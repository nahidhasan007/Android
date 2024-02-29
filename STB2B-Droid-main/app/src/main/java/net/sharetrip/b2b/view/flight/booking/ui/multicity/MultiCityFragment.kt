package net.sharetrip.b2b.view.flight.booking.ui.multicity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentMultiCityBinding
import net.sharetrip.b2b.util.*
import net.sharetrip.b2b.util.analytics.B2BEvent
import net.sharetrip.b2b.view.flight.booking.model.MultiCityModel
import net.sharetrip.b2b.view.flight.booking.model.TravellersInfo
import net.sharetrip.b2b.view.flight.booking.ui.search.FlightSearchFragmentDirections
import net.sharetrip.b2b.view.flight.booking.ui.travellers.FLIGHT_DATE
import net.sharetrip.b2b.view.flight.booking.ui.travellers.TRAVELLERS_INFO
import net.sharetrip.b2b.view.flight.booking.ui.travellers.TRAVELLERS_INFO_MULTI_CITY
import java.util.*
import kotlin.collections.ArrayList

class MultiCityFragment : Fragment(), MultiCityAdapter.MultiCityItemClickListener {
    private lateinit var bindingView: FragmentMultiCityBinding
    private val adapter = MultiCityAdapter()
    private var isOrigin = true
    private var today: Long = 0
    private var oneYearForward: Long = 0
    private var pickPosition = 0
    private lateinit var viewModel: MultiCityVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        bindingView = FragmentMultiCityBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(MultiCityVM::class.java)

        bindingView.viewModel = viewModel

        bindingView.recyclerViewMultiCity.adapter = adapter
        adapter.setMultiCityItemClickListener(this)

        getNavigationResultLiveData<TravellersInfo>(TRAVELLERS_INFO_MULTI_CITY)?.observe(
            viewLifecycleOwner
        ) {
            viewModel.updateTravellersAndClass(it)
        }

        bindingView.buttonAddCity.setOnClickListener {
            onAddCityButtonClicked()
        }

        bindingView.buttonRemoveCity.setOnClickListener {
            onRemoveCityButtonClicked()
        }

        bindingView.layoutTravelersAndClass.setOnClickListener {
            findNavController().navigate(
                R.id.action_flight_search_dest_to_travellers_fragment,
                bundleOf(
                    TRIP_TYPE to OTHER,
                    FLIGHT_DATE to multiCityList[0].departDate,
                    TRAVELLERS_INFO to viewModel.travellersInfo.get()
                )
            )
        }

        bindingView.buttonSearchFlight.setOnClickListener {
            checkAndStartFlightSearching()
        }

        return bindingView.root
    }

    private fun checkAndStartFlightSearching() {
        val origins = arrayListOf<String>()
        val destinations = arrayListOf<String>()
        val departDate = arrayListOf<String>()

        multiCityList.forEach {
            if (it.origin.isNotEmpty()) {
                origins.add(it.origin)
            } else {
                showMessage(requireContext().getString(R.string.origin_should_not_empty))
                return
            }

            if (it.destination.isNotEmpty()) {
                destinations.add(it.destination)
            } else {
                showMessage(requireContext().getString(R.string.destination_should_not_empty))
                return
            }

            if (it.departDate.isNotEmpty()) {
                departDate.add(it.departDate)
            } else {
                showMessage(requireContext().getString(R.string.departure_date_should_not_empty))
                return
            }
        }

        viewModel.flightSearchData.origin = Gson().toJson(origins)
        viewModel.flightSearchData.destination = Gson().toJson(destinations)
        viewModel.flightSearchData.flightDate = Gson().toJson(departDate)

        ShareTripB2B.getB2BAnalyticsManager(requireContext())
            .trackEvent(B2BEvent.FlightEvent.CLICK_ON_FLIGHT_SEARCH, viewModel.getFlightData())

        val action =
            FlightSearchFragmentDirections.actionFlightSearchDestToFlightListFragment(
                viewModel.flightSearchData
            )
        findNavController().navigate(action)
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun onAddCityButtonClicked() {
        multiCityList.add(MultiCityModel())
        adapter.notifyDataSetChanged()
        checkAddRemoveButtonVisibility()
    }

    private fun onRemoveCityButtonClicked() {
        multiCityList.removeAt(adapter.itemCount - 1)
        adapter.notifyDataSetChanged()
        checkAddRemoveButtonVisibility()
    }

    private fun checkAddRemoveButtonVisibility() {
        val isRemoveEnable = adapter.itemCount > 2
        viewModel.isRemoveButtonEnabled.set(isRemoveEnable)
        val isAddEnable = adapter.itemCount >= 5
        viewModel.isAddButtonEnabled.set(!isAddEnable)
    }

    override fun onFromItemClick(mPosition: Int) {
        isOrigin = true
        pickPosition = mPosition
        goToAirportSearch()
    }

    override fun onToItemClick(mPosition: Int) {
        isOrigin = false
        pickPosition = mPosition
        goToAirportSearch()
    }

    override fun onDateItemClick(mPosition: Int, origin: String, destination: String) {
        pickPosition = mPosition
        openSingleDateCalender()
    }

    override fun onSwapAirportClick(mPosition: Int, origin: String, destination: String) {
        pickPosition = mPosition
        multiCityList[pickPosition].origin = destination
        multiCityList[pickPosition].destination = origin
        adapter.notifyDataSetChanged()
    }

    private fun openSingleDateCalender() {
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText(requireContext().getString(R.string.select_travel_date))

        today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

        calendar.timeInMillis = today
        calendar.roll(Calendar.YEAR, 1)
        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        oneYearForward = calendar.timeInMillis

        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setOpenAt(today)
        constraintsBuilder.setStart(today)
        constraintsBuilder.setEnd(oneYearForward)
        constraintsBuilder.setValidator(DateValidatorPointForward.now())

        builder.setCalendarConstraints(constraintsBuilder.build())
        val datePicker = builder.build()
        datePicker.show(parentFragmentManager, "DATE_PICKER")

        datePicker.addOnPositiveButtonClickListener {
            val mStartDate = DateUtil.parseApiDateFormatFromMillisecond(it)
            multiCityList[pickPosition].departDate = mStartDate
            viewModel.updateTravellersAndClass(TravellersInfo())
            adapter.notifyDataSetChanged()
        }
    }

    private fun goToAirportSearch() {
        findNavController().navigate(
            R.id.action_flight_search_to_airport_search,
            bundleOf(
                ORIGIN_OR_DEST to isOrigin, TRIP_TYPE to OTHER,
                MULTI_CITY_POSITION to pickPosition
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        multiCityList.clear()
    }

    companion object {
        var multiCityList = ArrayList<MultiCityModel>()
    }
}
