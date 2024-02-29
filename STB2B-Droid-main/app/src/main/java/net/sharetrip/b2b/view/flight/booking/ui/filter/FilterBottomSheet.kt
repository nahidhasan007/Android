package net.sharetrip.b2b.view.flight.booking.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.sharetrip.b2b.databinding.LayoutFilterBottomSheetBinding
import net.sharetrip.b2b.util.MsgUtils.deselect_all
import net.sharetrip.b2b.util.MsgUtils.flight_schedule
import net.sharetrip.b2b.util.MsgUtils.refundable_text
import net.sharetrip.b2b.util.MsgUtils.select_all
import net.sharetrip.b2b.util.MsgUtils.stop_text
import net.sharetrip.b2b.util.MsgUtils.true_text
import net.sharetrip.b2b.util.MsgUtils.weight_text
import net.sharetrip.b2b.util.ShareTripB2B
import net.sharetrip.b2b.util.analytics.B2BEvent
import net.sharetrip.b2b.util.analytics.B2BEvent.FlightEvent.FLIGHT_DATA_DIRECT_ROUTE
import net.sharetrip.b2b.util.analytics.B2BEvent.FlightEvent.FLIGHT_DATA_FLIGHT_SCHEDULE
import net.sharetrip.b2b.util.analytics.B2BEvent.FlightEvent.FLIGHT_DATA_NON_REFUNDABLE
import net.sharetrip.b2b.util.analytics.B2BEvent.FlightEvent.FLIGHT_DATA_ONE_PLUS_STOP
import net.sharetrip.b2b.util.analytics.B2BEvent.FlightEvent.FLIGHT_DATA_ONE_STOP
import net.sharetrip.b2b.util.analytics.B2BEvent.FlightEvent.FLIGHT_DATA_REFUNDABLE
import net.sharetrip.b2b.util.analytics.B2BEvent.FlightEvent.FLIGHT_DATA_WEIGHT
import net.sharetrip.b2b.view.flight.booking.model.FlightFilter
import net.sharetrip.b2b.view.flight.booking.model.Schedule
import net.sharetrip.b2b.view.flight.booking.ui.filter.FlightFilterFragment.Companion.ARG_FILTER_TITLE
import net.sharetrip.b2b.view.flight.booking.ui.filter.FlightFilterFragment.Companion.ARG_FLIGHT_FILTER

class FilterBottomSheet : BottomSheetDialogFragment() {
    private lateinit var bindingView: LayoutFilterBottomSheetBinding
    private lateinit var flightFilter: FlightFilter
    private lateinit var filterAdapter: FilterAdapter
    private var index = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        bindingView = LayoutFilterBottomSheetBinding.inflate(inflater, container, false)
        bindingView.textViewSelection.text = select_all

        (dialog as? BottomSheetDialog)?.behavior?.apply {
            isFitToContents = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        val bundle = arguments
        try {
            index = bundle!!.getInt(ARG_FILTER_TITLE)
            flightFilter = bundle.getParcelable(ARG_FLIGHT_FILTER)!!

            when (index) {
                FilterTypeEnum.STOPS.filterCode -> {
                    bindingView.textViewSelection.visibility = View.VISIBLE
                    bindingView.textViewTitle.text = stop_text
                    filterAdapter = FilterAdapter(flightFilter.stop)
                    bindingView.recyclerFilter.adapter = filterAdapter
                }
                FilterTypeEnum.TIME.filterCode -> {
                    bindingView.textViewSelection.visibility = View.GONE
                    val scheduleList = ArrayList<Schedule>()
                    val obj = Schedule(flightFilter.outboundList, flightFilter.returnList)
                    scheduleList.add(obj)
                    bindingView.textViewTitle.text = flight_schedule
                    filterAdapter = FilterAdapter(scheduleList as List<Any>)
                    bindingView.recyclerFilter.adapter = filterAdapter
                }
                FilterTypeEnum.WEIGHT.filterCode -> {
                    bindingView.textViewSelection.visibility = View.VISIBLE
                    bindingView.textViewTitle.text = weight_text
                    filterAdapter = FilterAdapter(flightFilter.weight)
                    bindingView.recyclerFilter.adapter = filterAdapter
                }
                FilterTypeEnum.REFUNDABLE.filterCode -> {
                    bindingView.textViewSelection.visibility = View.VISIBLE
                    bindingView.textViewTitle.text = refundable_text
                    filterAdapter = FilterAdapter(flightFilter.getFlightFilterModel())
                    bindingView.recyclerFilter.adapter = filterAdapter
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        bindingView.imageViewClose.setOnClickListener {
            dismiss()
        }

        bindingView.buttonFilterApply.setOnClickListener {
            when (index) {
                FilterTypeEnum.STOPS.filterCode -> {
                    FilterConstrains.stopCodeSets = filterAdapter.codeList as ArrayList<Int>
                }
                FilterTypeEnum.TIME.filterCode -> {
                    FilterConstrains.outboundCodeSets = filterAdapter.outBoundCodeList
                    FilterConstrains.returnCodeSets = filterAdapter.returnCodeList
                }
                FilterTypeEnum.WEIGHT.filterCode -> {
                    FilterConstrains.weightCodeSets =
                        filterAdapter.codeList as ArrayList<Int>
                }
                FilterTypeEnum.REFUNDABLE.filterCode -> {
                    val refundable: ArrayList<Int> = ArrayList()

                    for (refundCodes in filterAdapter.refundCodes){
                        refundable.add(refundCodes.value)
                    }
                    FilterConstrains.isRefundableCodeSets = refundable
                    FilterConstrains.refundableText.value =
                        when {
                            refundable.size>1 -> "${refundable.size} Selected"
                            refundable.size==1 -> filterAdapter.refundCodes[0].key
                            else -> "Any"
                        }
                }
            }
            setFilterData()
            dismiss()
        }

        FilterConstrains.isSelectAll.observe(this) {
            bindingView.textViewSelection.text = if (it) deselect_all else select_all
        }

        bindingView.textViewSelection.setOnClickListener {
            if (bindingView.textViewSelection.text.toString().equals(select_all, true)) {
                bindingView.textViewSelection.text = deselect_all
                filterAdapter.setSelection(true)
            } else {
                bindingView.textViewSelection.text = select_all
                filterAdapter.setSelection(false)
            }
            filterAdapter.notifyDataSetChanged()
        }

        return bindingView.root
    }

    private fun setFilterData() {
        when (index) {
            FilterTypeEnum.STOPS.filterCode -> {
                ShareTripB2B.getB2BAnalyticsManager(requireContext())
                    .trackEvent(B2BEvent.FlightEvent.CLICK_STOPS, getStopData())
            }
            FilterTypeEnum.TIME.filterCode -> {
                ShareTripB2B.getB2BAnalyticsManager(requireContext())
                    .trackEvent(B2BEvent.FlightEvent.CLICK_FLIGHT_SCHEDULE, getScheduleData())
            }
            FilterTypeEnum.WEIGHT.filterCode -> {
                ShareTripB2B.getB2BAnalyticsManager(requireContext())
                    .trackEvent(B2BEvent.FlightEvent.CLICK_FLIGHT_WEIGHT, getWeightData())
            }
            FilterTypeEnum.REFUNDABLE.filterCode -> {
                ShareTripB2B.getB2BAnalyticsManager(requireContext())
                    .trackEvent(B2BEvent.FlightEvent.CLICK_REFUNDABLE, getRefundableData())
            }
        }
    }

    private fun getWeightData(): Map<String, String>? {
        val list: HashMap<String, String>?
        return if (filterAdapter.codeList.size > 0) {
            list = HashMap()
            var string = ""
            filterAdapter.codeList.forEach {
                string += "$it,"
            }
            list[FLIGHT_DATA_WEIGHT] = string
            list
        } else {
            null
        }
    }

    private fun getScheduleData(): Map<String, String>? {
        val list: HashMap<String, String>?
        var string = ""
        return if (filterAdapter.outBoundCodeList.size > 0 || filterAdapter.returnCodeList.size > 0) {
            list = HashMap()
            filterAdapter.outBoundCodeList.forEach {
                string += "$it,"
            }
            filterAdapter.returnCodeList.forEach {
                string = it
            }
            list[FLIGHT_DATA_FLIGHT_SCHEDULE] = string
            list
        } else {
            null
        }
    }

    private fun getRefundableData(): HashMap<String, String>? {
        val list: HashMap<String, String>?
        return if (filterAdapter.codeList.size > 0) {
            list = HashMap()
            filterAdapter.codeList.forEach {
                if (it == true_text) {
                    list[FLIGHT_DATA_REFUNDABLE] = FLIGHT_DATA_REFUNDABLE
                } else {
                    list[FLIGHT_DATA_NON_REFUNDABLE] = FLIGHT_DATA_NON_REFUNDABLE
                }
            }
            list
        } else {
            null
        }
    }

    private fun getStopData(): HashMap<String, String>? {
        val list: HashMap<String, String>?
        if (filterAdapter.codeList.size > 0) {
            list = HashMap()
            filterAdapter.codeList.forEach {
                when (it) {
                    0 -> {
                        list[FLIGHT_DATA_DIRECT_ROUTE] = FLIGHT_DATA_DIRECT_ROUTE
                    }
                    1 -> {
                        list[FLIGHT_DATA_ONE_STOP] = FLIGHT_DATA_ONE_STOP
                    }
                    else -> {
                        list[FLIGHT_DATA_ONE_PLUS_STOP] = FLIGHT_DATA_ONE_PLUS_STOP
                    }
                }
            }
            return list
        } else {
            return null
        }
    }
}
