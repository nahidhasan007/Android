package net.sharetrip.b2b.view.flight.booking.ui.flightdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentFlightDetailsBinding
import net.sharetrip.b2b.localdb.LocalDataBase
import net.sharetrip.b2b.util.BAGGAGE_INDEX
import net.sharetrip.b2b.util.CANCELLATION_POLICY_INDEX
import net.sharetrip.b2b.util.SHOW_TOOLBAR
import net.sharetrip.b2b.view.flight.booking.model.Flight
import net.sharetrip.b2b.view.flight.booking.model.FlightSearch
import net.sharetrip.b2b.view.flight.booking.model.Flights
import net.sharetrip.b2b.view.flight.booking.ui.flightlist.FlightListFragment.Companion.ARG_FLIGHT_SEARCH_MODEL
import net.sharetrip.b2b.view.flight.booking.ui.rules.FlightRulesFragment.Companion.ARG_SEARCH_ID
import net.sharetrip.b2b.view.flight.booking.ui.rules.FlightRulesFragment.Companion.ARG_SELECTED_RULES
import net.sharetrip.b2b.view.flight.booking.ui.rules.FlightRulesFragment.Companion.ARG_SEQUENCE_CODE
import net.sharetrip.b2b.view.flight.booking.ui.rules.FlightRulesFragment.Companion.ARG_SESSION_ID
import net.sharetrip.b2b.view.flight.history.faredetails.FragmentFareDetails
import net.sharetrip.b2b.view.flight.history.model.PriceDetails
import net.sharetrip.b2b.widgets.ItemCommonPriceBreakDownView

class FlightDetailsFragment : Fragment(), FlightDetailsAdapter.ItemClickListener,
    View.OnClickListener {
    lateinit var bindingView: FragmentFlightDetailsBinding
    lateinit var flightSearch: FlightSearch
    lateinit var flights: Flights
    lateinit var priceDetails: PriceDetails
    var selectedRules = 0

    private val viewModel: FlightDetailsVM by lazy {
        val dao = LocalDataBase.getDataBase(requireContext()).flightSearchDao()
        FlightDetailsVMFactory(
            FlightDetailsRepo(dao),
            arguments?.getParcelable(ARG_FLIGHT_SEARCH_MODEL)!!
        ).create(FlightDetailsVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView = FragmentFlightDetailsBinding.inflate(inflater, container, false)
        flightSearch = arguments?.getParcelable(ARG_FLIGHT_SEARCH_MODEL)!!
        flights = flightSearch.flights!!
        TRIP_TYPE = flightSearch.tripType

        bindingView.flights = flights
        bindingView.flightSearch = flightSearch

        priceDetails = PriceDetails(
            null, flights.currency, flights.price, null,
            flights.discountAmount, flights.advanceIncomeTax, flights.originPrice, null,
            flights.originPrice, flights.finalPrice
        )

        bindingView.priceDetails = priceDetails

        bindingView.recyclerViewFlightDetails.adapter =
            flights.flight?.let { FlightDetailsAdapter(it, this) }

        bindingView.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        commonPriceBreakdown()

        bindingView.textViewBaggagePolicy.setOnClickListener(this)
        bindingView.textViewCancellationPolicy.setOnClickListener(this)
        bindingView.textViewPriceBreakdown.setOnClickListener(this)
        bindingView.includePriceBottomSheet.buttonContinue.setOnClickListener(this)

        return bindingView.root
    }

    private fun commonPriceBreakdown() {
        val view = ItemCommonPriceBreakDownView(requireContext())
        view.setPriceBreakDown(priceDetails)
        bindingView.includePriceBottomSheet.layoutCommonPriceContainer.addView(view)
    }

    private fun goToFlightRules() {
        findNavController().navigate(
            R.id.action_flight_details_to_flight_rules,
            bundleOf(
                ARG_SEARCH_ID to flightSearch.searchId,
                ARG_SESSION_ID to flightSearch.sessionId,
                ARG_SEQUENCE_CODE to flights.sequence,
                ARG_SELECTED_RULES to selectedRules
            )
        )
    }

    override fun onClickItem(flight: Flight, position: Int) {
        findNavController().navigate(
            R.id.action_flight_details_to_flight_segment,
            bundleOf(
                ARG_ITEM_FLIGHTS to flights,
                ARG_ITEM_FLIGHTS_SEGMENT_POSITION to position,
                SHOW_TOOLBAR to false
            )
        )
    }

    override fun onClick(v: View?) {
        when (v) {
            bindingView.includePriceBottomSheet.buttonContinue -> {
                viewModel.onContinueButtonClick()
                findNavController().navigate(
                    R.id.action_flight_details_to_passengers,
                    bundleOf(ARG_IS_DOMESTIC to flights.domestic)
                )
            }

            bindingView.textViewPriceBreakdown -> {
                findNavController().navigate(
                    R.id.action_flight_details_to_fare_details_dest, bundleOf(
                        FragmentFareDetails.ARGS_PRICE_BREAKDOWN to priceDetails,
                        FragmentFareDetails.ARGS_NAVIGATE_FROM_FLIGHT_DETAILS to true
                    )
                )
            }

            bindingView.textViewCancellationPolicy -> {
                selectedRules = CANCELLATION_POLICY_INDEX
                goToFlightRules()
            }

            bindingView.textViewBaggagePolicy -> {
                selectedRules = BAGGAGE_INDEX
                goToFlightRules()
            }
        }
    }

    companion object {
//        const val ARG_FLIGHT_SEARCH_MODEL = "ARG_FLIGHT_SEARCH_MODEL"
        const val ARG_ITEM_FLIGHTS = "flight_details"
        const val ARG_ITEM_FLIGHTS_SEGMENT_POSITION = "selected_position"
        const val ARG_IS_DOMESTIC = "isDomestic"
        var TRIP_TYPE: String = ""
    }
}