package net.sharetrip.b2b.view.flight.history.reissue_change_date.multicity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import net.sharetrip.b2b.R
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.databinding.ReissueFlightSearchLayoutBinding
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.util.getNavigationResultLiveData
import net.sharetrip.b2b.view.flight.booking.model.TravellersInfo
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ReissueChangeDateSharedViewModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.CalenderData
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.DestinationPath
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlightSearch
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueMultiCityModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.multicity.MultiCityViewModel.Companion.ARG_FLIGHT_LIST_DATA
import net.sharetrip.b2b.view.flight.history.reissue_change_date.multicity.MultiCityViewModel.Companion.GOTO_FLIGHT_LIST
import net.sharetrip.b2b.view.flight.history.reissue_change_date.multicity.MultiCityViewModel.Companion.GOTO_TERMS
import net.sharetrip.b2b.view.flight.history.reissue_change_date.multicity.MultiCityViewModel.Companion.GOTO_TRAVELLER
import net.sharetrip.b2b.view.flight.history.reissue_change_date.multicity.MultiCityViewModel.Companion.PICK_DEPARTURE_DATE_REQUEST
import net.sharetrip.b2b.view.flight.history.reissue_change_date.searchairport.SearchAirportFragment.Companion.ARG_FLIGHT_SEARCH_BUNDLE
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.ARG_CALENDER_DATA
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.ARG_FLIGHT_SEARCH_TITLE_TEXT
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.ARG_SINGLE_DATE
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.ARG_TRAVELLER
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.NetworkUtil
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.setRoundCornerShapeDrawable
import net.sharetrip.b2b.widgets.BaseFragment

class MultiCityFragment : BaseFragment<ReissueFlightSearchLayoutBinding>() {
    companion object {
        const val TAG = "MultiCityFragment"

        const val ARG_FLIGHT_SEARCH_MULTI_CITY = "ARG_FLIGHT_SEARCH_MULTI_CITY"
        const val ARG_CLASS_NAME = "ARG_CLASS_NAME"
        const val DATE_CHANGE_POLICY = "DATE_CHANGE_POLICY"
        const val TERMS_AND_CONDITION = "TERMS_AND_CONDITION"
    }

    private val sharedViewModel: ReissueChangeDateSharedViewModel by activityViewModels()


    private val viewModel: MultiCityViewModel by lazy {
        MultiCityViewModelFactory(
            getString(R.string.origin_city_or_Airport),
            getString(R.string.destination_city_or_airport),
            getString(R.string.origin_city_or_Airport),
            getString(R.string.departure_date),
            sharedViewModel.selectedFlights.value ?: ArrayList()  // todo: change it!
        ).create(MultiCityViewModel::class.java)
    }

//    private var _bindingView: ReissueFlightSearchLayoutBinding? = null
//    private val bindingView: ReissueFlightSearchLayoutBinding get() = _bindingView!!

    private var isOrigin = true

    private val mMultiCityAdapter: MultiCityAdapter by lazy {
        MultiCityAdapter(
            mutableListOf(),
            sharedViewModel.reissueEligibilityResponse!!,
            viewModel::onFromItemClick,
            viewModel::onToItemClick,
            viewModel::onDateItemClick
        )
    }

    override fun layoutId(): Int = R.layout.reissue_flight_search_layout

    override fun getViewModel(): BaseViewModel? = null

    override fun initOnCreateView() {
        bindingView.lifecycleOwner = viewLifecycleOwner
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (sharedViewModel.reissueEligibilityResponse!!.skipSelection) {
            findNavController().navigate(
                R.id.action_reissueFlightSearchFragment_to_flightListFragment2,
                bundleOf(
                    ARG_FLIGHT_LIST_DATA to (ReissueFlightSearch())
                )
            )
            return
        }
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _bindingView = ReissueFlightSearchLayoutBinding.inflate(inflater, container, false)
//        bindingView.root.setOnClickListener { }
//        return bindingView.root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingView.viewModel = viewModel
        bindingView.sharedViewModel = sharedViewModel
        viewModel.promotionalImage = ""

        bindingView.multiCityRecyclerView.adapter = mMultiCityAdapter


        val mDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        val mItemDrawable =
            ContextCompat.getDrawable(requireActivity(), R.drawable.space_item_decorator_vertical)
        mDecoration.setDrawable(mItemDrawable!!)
        bindingView.multiCityRecyclerView.addItemDecoration(mDecoration)

        viewModel.isCheckboxActive.observe(viewLifecycleOwner) {
            if (it) {
                sharedViewModel.termsAndConditionCheckbox.postValue(true)
            }
            else {
                sharedViewModel.termsAndConditionCheckbox.postValue(false)
            }
        }


        viewModel.isAirportLayoutClicked.observe(viewLifecycleOwner, EventObserver {
            isOrigin = it
            openSearch()
        })
        sharedViewModel.onSearchClick.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                if (viewModel.isCheckboxActive.value == true) {
                    viewModel.onSearchFlightButtonClicked()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please acknowledge the date change policy",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        viewModel.changeItemAtPos.observe(viewLifecycleOwner) {
            mMultiCityAdapter.changeItemAtPos(it.first, it.second)
        }

        viewModel.navigateToDestination.observe(viewLifecycleOwner, EventObserver {
            when (it.first) {
                PICK_DEPARTURE_DATE_REQUEST -> {
                    findNavController().navigate(
                        R.id.action_reissueFlightSearchFragment_to_singleDateCalendarFragment2,
                        bundleOf(ARG_CALENDER_DATA to it.second as CalenderData)
                    )
                }

                GOTO_FLIGHT_LIST ->
                    if (NetworkUtil.hasNetwork(requireContext())) {
                        findNavController().navigate(
                            R.id.action_reissueFlightSearchFragment_to_flightListFragment2,
                            bundleOf(
                                ARG_FLIGHT_LIST_DATA to (it.second as ReissueFlightSearch)
                            )
                        )
                    } else {
                        Toast.makeText(context, "No Internet", Toast.LENGTH_LONG).show()
                    }

                GOTO_TRAVELLER -> {
                    findNavController().navigate(
                        R.id.action_reissueFlightSearchFragment_to_travellerNumberFragment2,
                        bundleOf(ARG_TRAVELLER to it.second as TravellersInfo)
                    )
                }

                GOTO_TERMS -> {
//                    val intent = Intent(context, ProfileActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    intent.putExtra(
//                        ProfileActivity.ARG_PROFILE_ACTION,
//                        ProfileAction.ARG_CONTENT_TERM
//                    )
//                    startActivity(intent)
                }

                else -> {

                }
            }
            /* todo: uncomment it

            when (it.first) {
                PICK_DEPARTURE_DATE_REQUEST ->
                    findNavController().navigate(
                        R.id.action_home_to_single_date_calender,  // todo: need this!
                        bundleOf(ARG_CALENDER_DATA to it.second as CalenderData)
                    )

                GOTO_FLIGHT_LIST ->
                    if (NetworkUtil.hasNetwork(requireContext())) {
                        findNavController().navigate(
                            R.id.action_go_to_flight_list, bundleOf(
                                ARG_FLIGHT_LIST_DATA to (it.second as FlightSearch)
                            )
                        )
                    } else {
                        Toast.makeText(context, "No Internet", Toast.LENGTH_LONG).show()
                    }

                GOTO_TRAVELLER ->
                    findNavController().navigate(
                        R.id.action_home_to_travellerNumber,
                        bundleOf(ARG_TRAVELLER to it.second as TravellersInfo)
                    )
            }
            */
        })


        viewModel.loadMulticityAdapter.observe(viewLifecycleOwner, EventObserver {
            sharedViewModel.selectedFlights.value?.map {
                mMultiCityAdapter.addItem(
                    ReissueMultiCityModel(
                        origin = it.origin?.code ?: "",
                        destination = it.destination?.code ?: "",
                        departDate = it.departure?.dateTime ?: "",
                        originAirport = it.origin?.airport,
                        destinationAirport = it.destination?.airport,
                        originCity = it.origin?.city,
                        destinationCity = it.destination?.city
                    )
                )
            }
        })

        setClickableTermsAndCondition()

        getNavigationResultLiveData<Long>(ARG_SINGLE_DATE)?.observe(viewLifecycleOwner) {
            viewModel.handleDepartureDate(it)
        }


        getNavigationResultLiveData<Intent>(ARG_TRAVELLER)?.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.handleTravellerData(it)
            }
        }

    }

    private fun setClickableTermsAndCondition() {
        val spannableString =
            SpannableString(requireContext().getString(R.string.reissue_policy))
        val termsClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                Log.d("Terms", "We are now showing terms and condition")
                viewModel.onClickTermsAndCondition()

            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                if (bindingView.tvTermsCondition.isPressed) {
                    ds.color = ContextCompat.getColor(requireContext(), R.color.clear_blue)
                } else {
                    ds.color = ContextCompat.getColor(requireContext(), R.color.clear_blue)
                }
                ds.isUnderlineText = false
                bindingView.tvTermsCondition.invalidate()
            }
        }
        val privacyPolicyClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                findNavController().navigate(
                    R.id.action_reissueFlightSearchFragment_to_dateChangePolicy
                )
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                if (bindingView.tvTermsCondition.isPressed) {
                    ds.color = ContextCompat.getColor(requireContext(), R.color.clear_blue)
                } else {
                    ds.color = ContextCompat.getColor(requireContext(), R.color.clear_blue)
                }
                ds.isUnderlineText = false
                bindingView.tvTermsCondition.invalidate()

            }
        }
        spannableString.setSpan(termsClickableSpan, 42, 61, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(
            privacyPolicyClickableSpan,
            18,
            36,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        bindingView.tvTermsCondition.text = spannableString
        bindingView.tvTermsCondition.movementMethod = LinkMovementMethod.getInstance()

    }


    private fun openSearch() {
        val bundle = Bundle()
        bundle.putString(ARG_FLIGHT_SEARCH_TITLE_TEXT, getString(R.string.origin_city_or_Airport))
        bundle.putString(ARG_CLASS_NAME, ARG_FLIGHT_SEARCH_MULTI_CITY)
        findNavController().navigate(
            R.id.action_home_to_flight_search,
            bundleOf(ARG_FLIGHT_SEARCH_BUNDLE to bundle)
        )
    }

    private fun makeTopLayoutRound() {
        bindingView.root.context.setRoundCornerShapeDrawable(
            view = bindingView.layoutTop,
            bottomLeft = 16,
            bottomRight = 16,
            color = Color.WHITE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}