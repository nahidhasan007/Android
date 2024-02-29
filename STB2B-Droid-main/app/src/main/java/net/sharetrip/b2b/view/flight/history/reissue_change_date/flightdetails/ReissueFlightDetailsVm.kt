package net.sharetrip.b2b.view.flight.history.reissue_change_date.flightdetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ApiCallingKey
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ConfirmReissueBody
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.FlightX
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlightSearch
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueManualCancelBody
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueManualCancelResponse
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueQuotationRequestSelectBody
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueQuotationRequestSelectResponse
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService
import net.sharetrip.b2b.widgets.BaseOperationalVm

class ReissueFlightDetailsVm(val flightSearch: ReissueFlightSearch, val apiService: ReissueApiService, val token : String) :
    BaseOperationalVm() {

    var flights: FlightX? = null
    val gotoSegment = MutableLiveData<Event<Int>>()

    val checkReissueConfirm = MutableLiveData(false)
    val approveObserver = MutableLiveData(false)
    val manualCancelResponse = MutableLiveData<Event<ReissueManualCancelResponse>>()
    val manualCancelResponseCheck = MutableLiveData(false)
    var reissueSearchId = ""
    var reissueSequenceCode = ""
    var isQuoted : Boolean = false

    private val _reissueQuotationRequestSelectResponse: MutableLiveData<Event<ReissueQuotationRequestSelectResponse?>> by lazy  { MutableLiveData(Event(null)) }
    val reissueQuotationRequestSelectResponse: LiveData<Event<ReissueQuotationRequestSelectResponse?>> get() = _reissueQuotationRequestSelectResponse
    fun onclickReissueManualConfirm() {
        val confirmReissueBody = ConfirmReissueBody(
            reissueSearchId,
            reissueSequenceCode
        )
        executeSuspendedCodeBlock(ApiCallingKey.ReissueConfirm.name) {
            apiService.confirmReissue(token, confirmReissueBody)
        }
    }

    fun onClickQuotationSelectApi(
        bookingCode: String,
        reissueSearchId: String,
        sequenceCode: String?
    ) {
        val reissueQuotationRequestSelectBody = ReissueQuotationRequestSelectBody(bookingCode, reissueSearchId, sequenceCode?:"")
        executeSuspendedCodeBlock(ApiCallingKey.ReissueQuotationSelect.name) {
            apiService.reissueRequestQuotationSelect(
                key = token, reissueQuotationRequestSelectBody
            )
        }
    }

    fun onclickReissueManualCancel(){
        val cancelManualReqBody = ReissueManualCancelBody(
            reissueSearchId
        )
        executeSuspendedCodeBlock(ApiCallingKey.ReissueCancel.name) {
            apiService.cancelReissueManaualRequest(token,cancelManualReqBody)
        }
    }

    fun onClickApprove(){
        approveObserver.value = true
    }




    fun gotoSegmentFragment(scrollToPosition: Int) {
        var position = scrollToPosition
        if (position > 0) {
            position = (flights?.segments?.size?:0) + 1  // question: 1 add korse keno? o.O
        }
        gotoSegment.value = Event(position)
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when(operationTag){
            ApiCallingKey.ReissueConfirm.name -> {
                val response = (data.body as RestResponse<*>).response
                checkReissueConfirm.value = true

            }
            ApiCallingKey.ReissueCancel.name -> {
                manualCancelResponse.postValue(Event(((data.body as RestResponse<*>).response as ReissueManualCancelResponse)))
                manualCancelResponseCheck.value = true
            }

            ApiCallingKey.ReissueQuotationSelect.name -> {
                Log.d(TAG, "onSuccess reissue quotation select: data = $data")
                val response = (data.body as RestResponse<*>).response
                _reissueQuotationRequestSelectResponse.postValue(Event(response as ReissueQuotationRequestSelectResponse))
            }
        }
    }
}