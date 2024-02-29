package net.sharetrip.b2b.view.flight.history.changelistfilter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentChangeTicketListFilterBinding
import net.sharetrip.b2b.util.DateUtils
import net.sharetrip.b2b.view.flight.history.changelistfilter.ChangedFlightFilterBottomSheet.Companion.ARGUMENT_CHECKED_DATA
import net.sharetrip.b2b.view.flight.history.changelistfilter.ChangedFlightFilterBottomSheet.Companion.RESULT_CHECKED_DATA
import net.sharetrip.b2b.view.flight.history.model.BookingDate
import net.sharetrip.b2b.view.flight.history.model.ChangedFlightFilter
import java.util.*

class ChangeTicketListFilterFragment : Fragment(), View.OnClickListener {

    lateinit var bindingView: FragmentChangeTicketListFilterBinding
    var changedFlightFilter: ChangedFlightFilter? = ChangedFlightFilter()
    val filterType = arrayListOf("Void", "Refund", "Change", "TemporaryCancel")
    val filterStatus = arrayListOf(
        "Searching", "Pending", "Processing",
        "Processed", "Completed", "Declined By ST", "Declined By Agency",
        "Reissue Requested", "Refund Price Updated", "Reissue Price Approved",
        "Refund Price Approved", "Processed By Flight Team"
    )
    private var dates = ArrayList<String>()
    private var selectedIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView =
            FragmentChangeTicketListFilterBinding.inflate(layoutInflater, container, false)
        val filterData = arguments?.getString(ARG_CHANGED_FLIGHT_FILTER_OBJECT)
        if (!filterData.isNullOrEmpty()) {
            changedFlightFilter = Gson().fromJson(filterData, ChangedFlightFilter::class.java)
        }

        bindingView.filterData = changedFlightFilter

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.buttonFilterApply.setOnClickListener(this)
        bindingView.includeRequestType.layoutArrow.setOnClickListener(this)
        bindingView.includeRequestStatus.layoutArrow.setOnClickListener(this)
        bindingView.includeBookingDate.layoutArrow.setOnClickListener(this)
        bindingView.tvReset.setOnClickListener(this)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(
            RESULT_CHECKED_DATA
        )?.observe(viewLifecycleOwner) { result ->
            val flightFilter = result.getString(ARGUMENT_CHECKED_DATA)
            when (selectedIndex) {
                ChangedFlightFilterTypeEnum.REQUEST_TYPE.filterCode -> {
                    changedFlightFilter?.requestType = flightFilter
                    bindingView.includeRequestType.hasSubtitle = true
                    bindingView.includeRequestType.subTitle = flightFilter
                }

                ChangedFlightFilterTypeEnum.REQUEST_STATUS.filterCode -> {
                    changedFlightFilter?.requestStatus = flightFilter
                    bindingView.includeRequestStatus.hasSubtitle = true
                    bindingView.includeRequestStatus.subTitle = flightFilter
                }
            }
        }

        return bindingView.root
    }

    override fun onClick(view: View?) {
        when (view) {
            bindingView.includeRequestType.layoutArrow -> {
                selectedIndex = ChangedFlightFilterTypeEnum.REQUEST_TYPE.filterCode
                val bundle = Bundle()
                bundle.putStringArrayList(ARG_CHANGED_FLIGHT_FILTER_DATA_LIST, filterType)
                bundle.putInt(
                    ARG_CHANGED_FLIGHT_FILTER_TITLE,
                    ChangedFlightFilterTypeEnum.REQUEST_TYPE.filterCode
                )
                findNavController().navigate(
                    R.id.action_open_changed_flight_filter_bottom_sheet,
                    bundle
                )
            }
            bindingView.includeRequestStatus.layoutArrow -> {
                selectedIndex = ChangedFlightFilterTypeEnum.REQUEST_STATUS.filterCode
                val bundle = Bundle()
                bundle.putStringArrayList(ARG_CHANGED_FLIGHT_FILTER_DATA_LIST, filterStatus)
                bundle.putInt(
                    ARG_CHANGED_FLIGHT_FILTER_TITLE,
                    ChangedFlightFilterTypeEnum.REQUEST_STATUS.filterCode
                )
                findNavController().navigate(
                    R.id.action_open_changed_flight_filter_bottom_sheet,
                    bundle
                )
            }
            bindingView.includeBookingDate.layoutArrow -> {
                showDateSelection()
            }
            bindingView.buttonFilterApply -> {
                setDataForResult(view)
            }
            bindingView.tvReset -> {
                changedFlightFilter = ChangedFlightFilter()
                bindingView.filterData= changedFlightFilter
            }
        }
    }

    private fun setDataForResult(view: View) {
        val bundle = Bundle()
        changedFlightFilter?.uuid = bindingView.includeRequestCode.editText.text.toString()
        changedFlightFilter?.bookingCode = bindingView.includeBookingCode.editText.text.toString()
        bundle.putParcelable(ARG_CHANGED_FLIGHT_FILTER_OBJECT, changedFlightFilter)
        view.findNavController().previousBackStackEntry?.savedStateHandle?.set(
            RESULT_CHANGED_FLIGHT_FILTER,
            bundle
        )
        view.findNavController().popBackStack()
    }

    private fun showDateSelection() {
        val calendar = Calendar.getInstance()
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val today = MaterialDatePicker.todayInUtcMilliseconds()

        calendar.add(Calendar.YEAR, -1)
        val startDate = calendar.timeInMillis
        calendar.timeInMillis = today

        calendar.add(Calendar.YEAR, 1)
        val endDate = calendar.timeInMillis

        if (dates.size > 1) {
            val flightDates = dates
            builder.setSelection(
                Pair(DateUtils.increaseDay(flightDates[0]), DateUtils.increaseDay(flightDates[1]))
            )
        }

        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setOpenAt(System.currentTimeMillis())
        constraintsBuilder.setStart(startDate)
        constraintsBuilder.setEnd(endDate)
        builder.setCalendarConstraints(constraintsBuilder.build())

        val datePicker = builder.build()
        datePicker.addOnPositiveButtonClickListener {
            dates = arrayListOf(
                DateUtils.millisecondsToString(it.first, DateUtils.API_DATE_PATTERN)!!,
                DateUtils.millisecondsToString(it.second, DateUtils.API_DATE_PATTERN)!!
            )
            val bookingDate = BookingDate(dates[0], dates[1])
            changedFlightFilter!!.bookingDate = bookingDate
            bindingView.includeBookingDate.hasSubtitle = true
            bindingView.includeBookingDate.subTitle =
                DateUtils.apiToDisplayDateFormat(dates[0]) + " - " + DateUtils.apiToDisplayDateFormat(
                    dates[1]
                )
        }
        datePicker.show(parentFragmentManager, datePicker.toString())
    }

    companion object {
        const val ARG_CHANGED_FLIGHT_FILTER_TITLE = "arg_changed_flight_filter_title"
        const val ARG_CHANGED_FLIGHT_FILTER_DATA_LIST = "arg_changed_flight_filter_data_list"
        const val ARG_CHANGED_FLIGHT_FILTER_OBJECT = "arg_changed_flight_filter_object"
        const val RESULT_CHANGED_FLIGHT_FILTER = "result_changed_flight_filter"
    }
}