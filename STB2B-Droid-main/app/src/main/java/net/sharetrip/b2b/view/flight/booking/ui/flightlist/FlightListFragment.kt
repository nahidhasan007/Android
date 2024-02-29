package net.sharetrip.b2b.view.flight.booking.ui.flightlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentFlightSearchListBinding
import net.sharetrip.b2b.localdb.LocalDataBase
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.network.Status
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.util.ShareTripB2B
import net.sharetrip.b2b.util.analytics.B2BEvent
import net.sharetrip.b2b.util.analytics.B2BEvent.FlightEvent.FLIGHT_DETAILS_DATA_AIRLINE_NAME
import net.sharetrip.b2b.util.analytics.B2BEvent.FlightEvent.FLIGHT_DETAILS_DATA_REFUNDABLE
import net.sharetrip.b2b.view.flight.booking.model.FilterParams
import net.sharetrip.b2b.view.flight.booking.model.FlightSearch
import net.sharetrip.b2b.view.flight.booking.model.Flights
import net.sharetrip.b2b.view.flight.booking.model.SortingType
import net.sharetrip.b2b.view.flight.booking.ui.filter.FlightFilterFragment
import net.sharetrip.b2b.view.flight.booking.ui.flightlist.FlightAdapter.OnItemClick

class FlightListFragment : Fragment(), AirlinesListener {
    private val flightSearchArgs by navArgs<FlightListFragmentArgs>()
    private val flightAdapter = FlightAdapter()
    private var selectedFlightList: ArrayList<Flights> = ArrayList()
    private lateinit var bindingView: FragmentFlightSearchListBinding
    private lateinit var slideUp: Animation
    private lateinit var airlineAdapter: AirlinesAdapter
    private lateinit var flightSearch: FlightSearch
    var isMakeProposalActive = false
    private var isFirstTransition = true

    private val flightListViewModel: FlightListVM by lazy {
        val endPoint = ServiceGenerator.createService(FlightEndPoint::class.java)
        val dao = LocalDataBase.getDataBase(requireContext()).passengerDao()
        FlightListVMFactory(
            FlightListRepo(
                endPoint,
                flightSearch,
                dao
            )
        ).create(FlightListVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        flightSearch = flightSearchArgs.flightSearch
        bindingView = FragmentFlightSearchListBinding.inflate(inflater, container, false)

        bindingView.viewModel = flightListViewModel
        bindingView.flightSearch = flightSearch
        bindingView.lifecycleOwner = viewLifecycleOwner

        bindingView.recyclerFlightList.adapter = flightAdapter
        airlineAdapter = AirlinesAdapter(this)
        bindingView.recyclerAirlines.adapter = airlineAdapter
        slideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(
            FlightFilterFragment.RESULT_FLIGHT_FILTER
        )?.observe(viewLifecycleOwner) { result ->
            val filterFilter: FilterParams? =
                result.getParcelable(FlightFilterFragment.FLIGHT_FILTER_PARAMS_ARGS)

            if (filterFilter?.stops != null || filterFilter?.weight != null || filterFilter?.outbound != null ||
                filterFilter?.returnTime != null || filterFilter?.isRefundable!= null
            ) {
                flightListViewModel.handleFilter(filterFilter)
            }
        }

        flightAdapter.setItemClick(object : OnItemClick {
            override fun onItemClick(view: View?, flights: Flights?, position: Int) {
                if (isMakeProposalActive) {
                    toggleSelection(position)
                } else {
                    ShareTripB2B.getB2BAnalyticsManager(requireContext())
                        .trackEvent(
                            B2BEvent.FlightEvent.CLICK_FLIGHT_DETAILS,
                            getFlightDetailsData(
                                flights?.flight?.get(0)?.airlines?.fullName,
                                flights?.refundable
                            )
                        )

                    if (flights != null) {
                        flightSearch.flights = flights
                        findNavController().navigate(
                            R.id.action_flight_list_to_flight_details,
                            bundleOf(ARG_FLIGHT_SEARCH_MODEL to flightSearch)
                        )
                    } else {
                        showToastMessage(MsgUtils.dataNotAvailableMsg)
                    }
                }
            }

            override fun onLongPress(view: View?, flights: Flights?, position: Int) {
            }
        })
        return bindingView.root
    }

    private fun getFlightDetailsData(fullName: String?, refundable: Boolean?): Map<String, String> {
        val list = HashMap<String, String>()
        list[FLIGHT_DETAILS_DATA_AIRLINE_NAME] = fullName ?: ""
        list[FLIGHT_DETAILS_DATA_REFUNDABLE] = (refundable ?: "").toString()
        return list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindingView.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        bindingView.textViewFilter.setOnClickListener { view ->
            ShareTripB2B.getB2BAnalyticsManager(requireContext())
                .trackEvent(B2BEvent.FlightEvent.CLICK_ON_FILTER)

            flightAdapter.clearSelectedItem()
            val action = FlightListFragmentDirections.actionFlightListToFlightFilter(
                flightListViewModel.totalFlightNumber.get()!!,
                flightListViewModel.flightFilter,
                flightSearch
            )
            view.findNavController().navigate(action)
        }

        bindingView.textViewSort.setOnClickListener {
            ShareTripB2B.getB2BAnalyticsManager(requireContext())
                .trackEvent(B2BEvent.FlightEvent.CLICK_ON_SORT)

            flightListViewModel.onSortingBtnClick(SortingType.NONE)
        }

        flightListViewModel.flightList.observe(viewLifecycleOwner, EventObserver {
            flightAdapter.resetItems(it)
        })

        flightListViewModel.airlineList.observe(viewLifecycleOwner, EventObserver {
            airlineAdapter.addAirlinesList(it)
        })

        flightListViewModel.showMessage.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

        flightListViewModel.dataState.observe(viewLifecycleOwner, EventObserver {
            when (it.status) {
                Status.RUNNING -> showLoadingStatus()

                Status.FAILED -> showEmptyDataStatus()

                Status.SUCCESS -> {
                    showDataLoadedStatus()
                    flightSearch.searchId = flightListViewModel.flightSearchID
                    flightSearch.sessionId = flightListViewModel.flightSessionID
                }

                Status.RESPONSE_EMPTY -> showEmptyDataStatus()
            }
        })

        bindingView.buttonContinueToProposal.setOnClickListener {
            gotToMakeProposalFragment(it)
        }

        bindingView.buttonMakeProposal.setOnClickListener {
            showProposalOrTapToSelectButton(true)
        }
    }

    override fun onResume() {
        super.onResume()
        val count = flightAdapter.selectedItemCount()
        if (count > 0) {
            bindingView.textViewFlightNumber.text =
                "$count " + requireContext().getString(R.string.flight_selected)
            bindingView.bottomSheetMakeProposal.visibility = VISIBLE
            bindingView.imageViewShadow.visibility = VISIBLE
            showProposalOrTapToSelectButton(true)
        } else {
            bindingView.textViewFlightNumber.text = ""
            bindingView.bottomSheetMakeProposal.visibility = GONE
            bindingView.imageViewShadow.visibility = GONE
            if (!isFirstTransition) {
                showProposalOrTapToSelectButton(false)
            }
        }
        isFirstTransition = false
    }

    private fun toggleSelection(position: Int) {
        flightAdapter.toggleSelection(position)
        val count = flightAdapter.selectedItemCount()

        if (count == 0) {
            bindingView.bottomSheetMakeProposal.visibility = GONE
            bindingView.imageViewShadow.visibility = GONE
            bindingView.textViewFlightNumber.text = ""
            showProposalOrTapToSelectButton(false)
        } else {
            bindingView.textViewFlightNumber.text =
                "$count " + requireContext().getString(R.string.flight_selected)
            bindingView.bottomSheetMakeProposal.visibility = VISIBLE
            bindingView.imageViewShadow.visibility = VISIBLE
        }
    }

    private fun showProposalOrTapToSelectButton(value: Boolean) {
        isMakeProposalActive = value

        if (isMakeProposalActive) {
            bindingView.buttonMakeProposal.visibility = GONE
            bindingView.buttonTapToSelect.visibility = VISIBLE
        } else {
            bindingView.buttonMakeProposal.visibility = VISIBLE
            bindingView.buttonTapToSelect.visibility = GONE
        }
    }

    private fun gotToMakeProposalFragment(view: View) {
        val action = FlightListFragmentDirections.actionFlightListFragmentToMakeProposalFragment(
            getFlightList().toTypedArray(), flightSearch
        )
        view.findNavController().navigate(action)
    }

    private fun getFlightList(): ArrayList<Flights> {
        selectedFlightList.clear()
        val temporaryList = ArrayList<Flights>()
        for (i in flightAdapter.getSelectedItems()) {
            temporaryList.add(flightAdapter.getItem(i))
        }
        selectedFlightList = temporaryList.map { it.copy() } as ArrayList<Flights>
        return selectedFlightList
    }

    private fun showDataLoadedStatus() {
        bindingView.includeShimmer.layoutShimmer.visibility = GONE
        bindingView.includeShimmer.layoutShimmer.stopShimmer()
        bindingView.groupDataAvailable.visibility = VISIBLE
    }

    private fun showLoadingStatus() {
        bindingView.groupDataAvailable.visibility = GONE
        bindingView.includeShimmer.layoutShimmer.visibility = VISIBLE
        bindingView.includeShimmer.layoutShimmer.startAnimation(slideUp)
        bindingView.layoutEmptyContainer.visibility = GONE
    }

    private fun showEmptyDataStatus() {
        bindingView.groupDataAvailable.visibility = GONE
        bindingView.includeShimmer.layoutShimmer.visibility = GONE
        bindingView.includeShimmer.layoutShimmer.stopShimmer()
        bindingView.layoutEmptyContainer.visibility = VISIBLE
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onCodeSelect(code: String) {
        ShareTripB2B.getB2BAnalyticsManager(requireContext())
            .trackEvent(B2BEvent.FlightEvent.CLICK_ON_PREFFERED_AIRLINES)
        val airlineList = ArrayList<String>()
        airlineList.add(code)
        flightListViewModel.handleAirlineFilter(airlineList)
    }

    companion object {
        const val ARG_FLIGHT_SEARCH_MODEL = "flight_search"
    }
}
