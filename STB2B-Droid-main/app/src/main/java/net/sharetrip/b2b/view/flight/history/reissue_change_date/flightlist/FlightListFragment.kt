package net.sharetrip.b2b.view.flight.history.reissue_change_date.flightlist

import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentReissueFlightListBinding
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.util.DateUtil
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.util.showToast
import net.sharetrip.b2b.view.flight.booking.model.FilterParams
import net.sharetrip.b2b.view.flight.booking.ui.flightdetails.FlightDetailsFragment
import net.sharetrip.b2b.view.flight.booking.ui.flightlist.AirlinesAdapter
import net.sharetrip.b2b.view.flight.booking.ui.flightlist.AirlinesListener
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ReissueChangeDateSharedViewModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.flightdetails.ReissueFlightDetailsFragment
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.Filters
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.FlightX
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlightBody
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlightSearch
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueMultiCityModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueQuotationRequestBody
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.SortingType
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.TripType
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.ItemClickSupport
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.MoshiUtil
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.PagingLoadStateAdapter
import java.io.IOException
import java.text.ParseException

class FlightListFragment : Fragment(), AirlinesListener {
    companion object {
        const val TAG = "FlightList"
        const val ARG_FLIGHT_LIST_DATA = "ARG_FLIGHT_LIST_DATA"
        const val ARG_REISSUE_SEARCH_ID = "ARG_REISSUE_SEARCH_ID"
        const val ARG_REISSUE_FLIGHT_SEARCH_MODEL = "ARG_REISSUE_FLIGHT_SEARCH_MODEL"
        const val ARG_FLIGHT_FILTER_INFO_STRING_MODEL = "ARG_FLIGHT_FILTER_INFO_STRING_MODEL"
        const val ARG_FLIGHT_FILTER_INFO_STRING_FLIGHT_COUNT =
            "ARG_FLIGHT_FILTER_INFO_STRING_FLIGHT_COUNT"
        const val ARG_FLIGHT_FILTER_BUNDLE = "ARG_FLIGHT_FILTER_BUNDLE"
    }

    private var _bindingView: FragmentReissueFlightListBinding? = null
    private val bindingView: FragmentReissueFlightListBinding get() = _bindingView!!

    private val sharedViewModel: ReissueChangeDateSharedViewModel by activityViewModels()
    private val flightSearch: ReissueFlightSearch by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(
                ARG_FLIGHT_LIST_DATA,
                ReissueFlightSearch::class.java
            )!!
        } else {
            @Suppress("DEPRECATION")
            requireArguments().getParcelable(ARG_FLIGHT_LIST_DATA)!!
        }
    }


    private val flightsAdapter: FlightAdapter by lazy {
        FlightAdapter()
    }

    private var isFirstTimeAirline = true
    private lateinit var slideUp: Animation
    private var isFirstTime = true
    private var isFirstPage = false
    private var sequenceCode = ""

    private val reissueApiService by lazy {
        ServiceGenerator.createService(ReissueApiService::class.java)
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            FlightListViewModelFactory(
                reissueApiService,
                flightSearch,
                FlightListRepository(
                    flightSearch,
                    reissueApiService,
                    AppSharedPreference.accessToken
                ),
                createQuotationRequest(),
                AppSharedPreference.accessToken
            )
        )[FlightListViewModel::class.java]
    }

    private fun createQuotationRequest(): ReissueQuotationRequestBody {
        if (sharedViewModel.reissueEligibilityResponse!!.reissueSearch?.status == "QUOTED")
            return ReissueQuotationRequestBody(bookingCode = sharedViewModel.bookingCode!!)

        return ReissueQuotationRequestBody(
            cabinClass = flightSearch.classType,
            bookingCode = sharedViewModel.reissueEligibilityResponse!!.bookingCode,
            reissueCode = null,
            isNowShow = true,
            travellers = createListOfTravellers(),
//            selection = sharedViewModel.selectedFlights.value!!.map { it.id },
            flights = flightSearch.multiCityModels.map { mcm: ReissueMultiCityModel ->
                ReissueFlightBody(
                    origin = mcm.origin,
                    destination = mcm.destination,
                    date = extractDateFromDateTime(mcm.departDate),
                    uuid = sharedViewModel.manualFlights[0].uuid,
                    flightStatusOptions = null
                )
            },
        )
    }

    private fun createListOfTravellers(): List<String> {

        val selectedPassengers = sharedViewModel.selectedPassengers.value
        val someList: List<String> = selectedPassengers?.map { someTraveller ->
            someTraveller.id
                ?: throw IllegalStateException("Traveller id must not be null, someTraveller = $someTraveller")
        }?.toList() ?: emptyList()

        return someList
    }

    private fun extractDateFromDateTime(dateTimeString: String): String {
        val dateTimeParts = dateTimeString.split(" ")
        val datePart = dateTimeParts[0]
        return datePart
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (flightSearch.classType.contains("First"))
            flightSearch.classType = "First"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingView = FragmentReissueFlightListBinding.inflate(inflater, container, false)
        bindingView.root.setOnClickListener {}
        return bindingView.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        viewModel.manualCancelResponseCheck.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    "Your reissue manual request has been canceled!",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }



        slideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
        bindingView.flightRecyclerView.adapter = flightsAdapter

        viewModel.airlinesList.observe(viewLifecycleOwner) { listAirlines ->
            if (isFirstTimeAirline) {
                val mAdapter = AirlinesAdapter(this)
                mAdapter.addAirlinesList(listAirlines)
                bindingView.airlinesRecycler.adapter = mAdapter
            }
            isFirstTimeAirline = false
        }

        viewModel.searchId?.observe(viewLifecycleOwner) { s: String? ->
            flightSearch.searchId = s!!
            showToast(requireContext(), "searchId = $s \nremove me later")
        }
        viewModel.expiresAt.observe(viewLifecycleOwner) {
            if (it != null) {
                sharedViewModel.quotationExpiryAt = it
            }
        }

        viewModel.isFirstPage.observe(viewLifecycleOwner) { isFirstPage = it }

        viewModel.sessionId.observe(viewLifecycleOwner) { s: String? ->
            flightSearch.sessionId = s!!
        }

        viewModel.totalRecordCount.observe(viewLifecycleOwner) { count: Int? ->
            if (count!! > 1) {
                bindingView.filterTitleTextView.text = getString(R.string.available_flights_found, count)
            } else {
                bindingView.filterTitleTextView.text = getString(R.string.available_flight_found, count)
            }

            viewModel.setNumberOfFlight(count!!)
        }

        viewModel.sortingObserver.observe(viewLifecycleOwner) {
            if (it != SortingType.NONE && !isFirstTime) {
                viewModel.handleSortingFilter(
                    it.toString().toLowerCase(),
                    flightSearch.searchId
                )
            }
            isFirstTime = false
        }

        viewModel.filterDeal.observe(viewLifecycleOwner) { data: String ->
            if (isFirstTime) {
                when (data) {
                    SortingType.FASTEST.toString() -> {
                        viewModel.sortingObserver.value = SortingType.FASTEST
                    }

                    SortingType.CHEAPEST.toString() -> {
                        viewModel.sortingObserver.value = SortingType.CHEAPEST
                    }

                    SortingType.EARLIEST.toString() -> {
                        viewModel.sortingObserver.value = SortingType.EARLIEST
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.flightList.collectLatest {
                flightsAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }

        viewModel.onFilterClicked.observe(viewLifecycleOwner, EventObserver {
            val bundle = Bundle()
            bundle.putString(
                ARG_FLIGHT_FILTER_INFO_STRING_MODEL, MoshiUtil.convertModelClassToString(
                    viewModel.filter, Filters::class.java
                )
            )
            bundle.putInt(ARG_FLIGHT_FILTER_INFO_STRING_FLIGHT_COUNT, viewModel.flightCount)

            /* looks like this is booking only feature
            findNavController().navigate(
                R.id.action_flightList_to_flightFilter,
                bundleOf(ARG_FLIGHT_FILTER_BUNDLE to bundle)
            )*/
        })

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(
            ARG_FLIGHT_FILTER_BUNDLE
        )?.observe(viewLifecycleOwner) { result ->
            result?.let {
                try {
                    val params = MoshiUtil.convertStringToModelClass(
                        result.getString(ARG_FLIGHT_FILTER_INFO_STRING_MODEL),
                        FilterParams::class.java
                    )
                    viewModel.handleFlightFilter(params, flightSearch.searchId)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        bindingView.includeShimmer.shimmerLayout.startShimmer()
        initRecyclerView()
        bindingView.lifecycleOwner = viewLifecycleOwner
    }

    override fun onCodeSelect(code: String) {
        val airlineList = ArrayList<String>()
        airlineList.add(code)
        viewModel.handleAirlineFilter(airlineList, flightSearch.searchId)
    }

    override fun onStart() {
        super.onStart()
        /*var points = SharedPrefsHelper(requireContext())[PrefKey.USER_TRIP_COIN, ""]
        points = points.filter { it in '0'..'9' }

        if (points.isBlank()) {
            points = "0"
            SharedPrefsHelper(requireContext()).put(PrefKey.USER_TRIP_COIN, "0")
        }

        setTripCoin(NumberFormat.getNumberInstance(Locale.US).format(points.toInt()))*/
    }

    private fun showRunningStatus() {
        bindingView.includeShimmer.shimmerLayout.visibility = View.VISIBLE
        bindingView.includeShimmer.shimmerLayout.startAnimation(slideUp)
        bindingView.flightRecyclerView.visibility = View.GONE
        bindingView.filterConstrainLayout.visibility = View.GONE
        bindingView.cardInfo.visibility = View.GONE
        bindingView.filterConstrainLayout.visibility = View.GONE
        bindingView.airlinesRecycler.visibility = View.GONE
    }

    private fun showFailedStatus(message: String) {
        bindingView.includeShimmer.shimmerLayout.visibility = View.GONE
        bindingView.includeShimmer.shimmerLayout.stopShimmer()
        bindingView.flightRecyclerView.visibility = View.GONE
        bindingView.cardInfo.visibility = View.VISIBLE
        bindingView.textViewInfoMsg.text = message
        bindingView.filterConstrainLayout.visibility = View.GONE
        bindingView.airlinesRecycler.visibility = View.GONE
    }

    private fun showDefaultStatus() {
        bindingView.includeShimmer.shimmerLayout.visibility = View.GONE
        bindingView.includeShimmer.shimmerLayout.stopShimmer()
        bindingView.flightRecyclerView.visibility = View.VISIBLE
        bindingView.filterConstrainLayout.visibility = View.VISIBLE
        bindingView.airlinesRecycler.visibility = View.VISIBLE
        bindingView.cardInfo.visibility = View.GONE
    }

    @OptIn(InternalCoroutinesApi::class)
    private fun initRecyclerView() {
        ItemClickSupport.addTo(bindingView.flightRecyclerView)
            .setOnItemClickListener { _: RecyclerView?, position: Int, _: View? ->
                val list = flightsAdapter.snapshot().items
                val flights: FlightX = list[position]
                sequenceCode = flights.reissueSequenceCode.toString()
                handleFlightItemSelect(flights)
            }
        bindingView.flightRecyclerView.adapter = flightsAdapter.withLoadStateFooter(
            footer = PagingLoadStateAdapter { flightsAdapter.retry() }
        )

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            flightsAdapter.loadStateFlow.collect { loadState ->
                if (loadState.refresh is LoadState.Loading) {
                    showRunningStatus()
                } else if (loadState.refresh is LoadState.Error && isFirstPage) {
                    (loadState.refresh as LoadState.Error).error.message?.let {
                        showFailedStatus(it)
                    }
                } else if (loadState.refresh is LoadState.NotLoading) {
                    showDefaultStatus()
                }
            }
        }

//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            flightsAdapter.loadStateFlow.collect {  }
//        }

    }

    private fun setTitle() {
        val mBuilder = SpannableStringBuilder()
        val destination = flightSearch.destination[flightSearch.destination.size - 1]
        val spannableDestination = SpannableString("  $destination")
        val mGroupSpan: ImageSpan = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val myDrawable =
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_dots_horizontal_24dp)
            myDrawable!!.setBounds(0, 0, myDrawable.intrinsicWidth, myDrawable.intrinsicHeight)
            ImageSpan(myDrawable, ImageSpan.ALIGN_BASELINE)
        } else {
            ImageSpan(requireContext(), R.drawable.ic_dots_horizontal_24dp)
        }

        spannableDestination.setSpan(mGroupSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val origin = flightSearch.origin[0]
        mBuilder.append("$origin ")
        mBuilder.append(spannableDestination)
        (activity as AppCompatActivity).supportActionBar?.title = mBuilder
    }

    private fun setInfoLabel() {
        val mBuilder = StringBuilder()
        val depart = flightSearch.depart
        if (flightSearch.tripType.equals(TripType.ONE_WAY, true)) {
            try {
                val mDate = DateUtil.parseDisplayDateMonthFormatFromApiDateFormat(depart[0])
                mBuilder.append(mDate)
                    .append(" " + flightSearch.numberOfTraveller.toString() + " Traveller(s)")
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        } else {
            try {
                val mStartDate = DateUtil.parseDisplayDateMonthFormatFromApiDateFormat(depart[0])
                val mEndDate =
                    DateUtil.parseDisplayDateMonthFormatFromApiDateFormat(depart[depart.size - 1])
                mBuilder.append(mStartDate).append(" - ").append(mEndDate)
                    .append(" " + flightSearch.numberOfTraveller.toString() + " Traveller(s)")
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        (activity as AppCompatActivity).supportActionBar?.subtitle = mBuilder
    }


    private fun handleFlightItemSelect(flights: FlightX) {
        sharedViewModel.flightToBeBooked = flights
        sharedViewModel.flightSearch = flightSearch

        flightSearch.sequence = flights.reissueSequenceCode ?: flights.sequenceCode!!
        val bundle = Bundle()

        val reissueFlightSearch = viewModel.flightSearch
        val searchId = viewModel.searchId?.value

        Log.d(ReissueFlightDetailsFragment.TAG, "reissueFlightSearch: $reissueFlightSearch")
        Log.d(ReissueFlightDetailsFragment.TAG, "flghtX: $flights")
        Log.d(ReissueFlightDetailsFragment.TAG, "searchId: $searchId")

        bundle.putParcelable(ARG_REISSUE_FLIGHT_SEARCH_MODEL, reissueFlightSearch)
        bundle.putParcelable(FlightDetailsFragment.ARG_ITEM_FLIGHTS, flights)
        bundle.putString(ARG_REISSUE_SEARCH_ID, searchId)



        findNavController().navigate(
            R.id.action_flightListFragment2_to_flightDetailsFragment2,
            bundle // bundleOf("ARG_SELECTED_FLIGHT" to bundle)
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _bindingView = null
    }
}