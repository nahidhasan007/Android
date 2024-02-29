package net.sharetrip.b2b.view.flight.booking.ui.filter

import androidx.lifecycle.MutableLiveData

class FilterConstrains {
    companion object {
        var isRefundableCodeSets: List<Int>? = null
        var refundableText = MutableLiveData<String>()
        var stopCodeSets: List<Int>? = ArrayList()
        var weightCodeSets: List<Int>? = ArrayList()
        var outboundCodeSets: List<String>? = ArrayList()
        var returnCodeSets: List<String>? = ArrayList()
        val isSelectAll = MutableLiveData<Boolean>()
    }
}