package net.sharetrip.b2b.view.flight.booking.ui.regreso

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentReturnBinding
import net.sharetrip.b2b.util.*
import net.sharetrip.b2b.util.DateUtils.API_DATE_PATTERN
import net.sharetrip.b2b.util.DateUtils.increaseDay
import net.sharetrip.b2b.util.DateUtils.millisecondsToString
import net.sharetrip.b2b.util.analytics.B2BEvent
import net.sharetrip.b2b.view.flight.booking.model.TravellersInfo
import net.sharetrip.b2b.view.flight.booking.ui.airport.AIRPORT_IATA_RETURN
import net.sharetrip.b2b.view.flight.booking.ui.search.FlightSearchFragmentDirections
import net.sharetrip.b2b.view.flight.booking.ui.travellers.FLIGHT_DATE
import net.sharetrip.b2b.view.flight.booking.ui.travellers.TRAVELLERS_INFO
import net.sharetrip.b2b.view.flight.booking.ui.travellers.TRAVELLERS_INFO_RETURN

class ReturnFragment : Fragment() {
    private var isOrigin: Boolean = true
    private lateinit var viewModel: ReturnVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentReturnBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ReturnVM::class.java)
        bindingView.lifecycleOwner = viewLifecycleOwner
        bindingView.returnVM = viewModel

        getNavigationResultLiveData<String>(AIRPORT_IATA_RETURN)?.observe(viewLifecycleOwner) {
            viewModel.updateAirport(isOrigin, it)
        }

        getNavigationResultLiveData<TravellersInfo>(TRAVELLERS_INFO_RETURN)?.observe(
            viewLifecycleOwner
        ) {
            viewModel.updateTravellers(it)
        }

        viewModel.moveToTravellers.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(
                R.id.action_flight_search_dest_to_travellers_fragment,
                bundleOf(
                    TRIP_TYPE to RETURN,
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
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            if (viewModel.dates.get()!!.size > 1) {
                val flightDates = viewModel.dates.get()
                builder.setSelection(
                    Pair(increaseDay(flightDates!![0]), increaseDay(flightDates[1]))
                )
            }

            val constraintsBuilder = CalendarConstraints.Builder()
            constraintsBuilder.setOpenAt(System.currentTimeMillis())
            constraintsBuilder.setStart(System.currentTimeMillis())
            constraintsBuilder.setValidator(DateValidatorPointForward.now())
            builder.setCalendarConstraints(constraintsBuilder.build())

            val datePicker = builder.build()
            datePicker.addOnPositiveButtonClickListener {
                viewModel.updateDate(
                    arrayListOf(
                        millisecondsToString(it.first, API_DATE_PATTERN)!!,
                        millisecondsToString(it.second, API_DATE_PATTERN)!!
                    )
                )

            }
            datePicker.show(parentFragmentManager, datePicker.toString())
        }

        bindingView.btnSearchFlight.setOnClickListener {
            ShareTripB2B.getB2BAnalyticsManager(requireContext())
                .trackEvent(B2BEvent.FlightEvent.CLICK_ON_FLIGHT_SEARCH, viewModel.getFlightData())

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
            bundleOf(ORIGIN_OR_DEST to isOrigin, TRIP_TYPE to RETURN)
        )
    }

}
