package net.sharetrip.b2b.view.flight.booking.ui.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FlightFilterVM : ViewModel() {
    val liveData =
        MutableLiveData<FilterTypeEnum>()

    fun onClickRefundableIcon() {
        liveData.value = FilterTypeEnum.REFUNDABLE
    }

    fun onClickStopIcon() {
        liveData.value = FilterTypeEnum.STOPS
    }

    fun onClickWeightIcon() {
        liveData.value = FilterTypeEnum.WEIGHT
    }

    fun onClickOnwardTimeIcon() {
        liveData.value = FilterTypeEnum.TIME
    }

    fun clearFilterData() {
        FilterConstrains.outboundCodeSets = null
        FilterConstrains.returnCodeSets = null
        FilterConstrains.isRefundableCodeSets = null
        FilterConstrains.refundableText.value = "Any"
        FilterConstrains.stopCodeSets = null
        FilterConstrains.weightCodeSets = null
    }
}
