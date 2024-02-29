package net.sharetrip.b2b.view.flight.booking.ui.oneway

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentOneWayBinding
import net.sharetrip.b2b.util.*
import net.sharetrip.b2b.util.DateUtils.getCalender
import net.sharetrip.b2b.util.analytics.B2BEvent.FlightEvent.CLICK_ON_FLIGHT_SEARCH
import net.sharetrip.b2b.view.flight.booking.model.TravellersInfo
import net.sharetrip.b2b.view.flight.booking.ui.airport.AIRPORT_IATA_ONE_WAY
import net.sharetrip.b2b.view.flight.booking.ui.search.FlightSearchFragmentDirections
import net.sharetrip.b2b.view.flight.booking.ui.travellers.FLIGHT_DATE
import net.sharetrip.b2b.view.flight.booking.ui.travellers.TRAVELLERS_INFO
import net.sharetrip.b2b.view.flight.booking.ui.travellers.TRAVELLERS_INFO_ONE_WAY

class OneWayFragment : Fragment() {
    private var isOrigin: Boolean = true
    private lateinit var viewModel: OneWayVM
    private lateinit var bindingView: FragmentOneWayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView = FragmentOneWayBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(OneWayVM::class.java)
        bindingView.lifecycleOwner = viewLifecycleOwner
        bindingView.viewModel = viewModel

        getNavigationResultLiveData<String>(AIRPORT_IATA_ONE_WAY)?.observe(viewLifecycleOwner) {
            viewModel.updateAirport(isOrigin, it)
        }
        getNavigationResultLiveData<TravellersInfo>(TRAVELLERS_INFO_ONE_WAY)?.observe(
            viewLifecycleOwner
        ) {
            viewModel.updateTravellers(it)
        }

        viewModel.moveToTravellers.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(
                R.id.action_flight_search_dest_to_travellers_fragment,
                bundleOf(
                    TRIP_TYPE to ONE_WAY,
                    FLIGHT_DATE to it.first,
                    TRAVELLERS_INFO to it.second
                )
            )
        })

        bindingView.layoutFrom.setOnClickListener {
            isOrigin = true
            goToAirportSearch()
        }

        bindingView.layoutTo.setOnClickListener {
            isOrigin = false
            goToAirportSearch()
        }

        bindingView.layoutDepartureDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(), { _, year, monthOfYear, dayOfMonth ->
                    val month = formatToTwoDigit(monthOfYear + 1)
                    val day = formatToTwoDigit(dayOfMonth)
                    viewModel.updateDate("$year-$month-$day")
                }, getCalender().year, getCalender().month, getCalender().day
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()
        }

        bindingView.btnSearchFlight.setOnClickListener {
            ShareTripB2B.getB2BAnalyticsManager(requireContext())
                .trackEvent(CLICK_ON_FLIGHT_SEARCH, viewModel.getFlightData())

            val action = FlightSearchFragmentDirections.actionFlightSearchDestToFlightListFragment(
                viewModel.getSearchQuery()
            )
            findNavController().navigate(action)
        }

        return bindingView.root
    }

    private fun goToAirportSearch() {
        findNavController().navigate(
            R.id.action_flight_search_to_airport_search,
            bundleOf(ORIGIN_OR_DEST to isOrigin, TRIP_TYPE to ONE_WAY)
        )
    }
}
