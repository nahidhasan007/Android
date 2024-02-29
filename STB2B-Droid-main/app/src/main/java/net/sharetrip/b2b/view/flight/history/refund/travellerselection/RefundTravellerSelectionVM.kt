package net.sharetrip.b2b.view.flight.history.refund.travellerselection

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.sync.Mutex
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.view.flight.history.model.RefundQuotationResponse
import net.sharetrip.b2b.view.flight.history.model.VRRQuotationResponse
import net.sharetrip.b2b.view.flight.history.refund.flightselection.RefundFlightSelectionVM
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ErrorType
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.RefundSearchIdBody
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller
import net.sharetrip.b2b.widgets.BaseOperationalVm
import retrofit2.http.Header
import retrofit2.http.Query

class RefundTravellerSelectionVM(
    private val token : String,
    private val apiService: FlightEndPoint
) : BaseOperationalVm() {
    companion object {
        const val TAG = "RefundTravellerSelectionVm"
        const val REFUND_QUOTATION_OPERATION_TAG = "REFUND_QUOTATION_OPERATION_TAG"
    }


    var refundConfirmResponse = MutableLiveData(false)

    val liveRefundQuotationResponse = MutableLiveData<Event<RefundQuotationResponse>>()

    private val _selectedPassengers = MutableLiveData<ArrayList<ReissueTraveller>>(arrayListOf())
    val selectedPassengers: LiveData<ArrayList<ReissueTraveller>>
        get() = _selectedPassengers
    fun addSelectedTraveller(traveller: ReissueTraveller) {
        val temp = _selectedPassengers.value
        temp?.let {
            it.add(traveller)
            _selectedPassengers.value = it
        }
    }

    fun removeTraveller(traveller: ReissueTraveller) {
        val temp = _selectedPassengers.value
        temp?.let {
            it.remove(traveller)
            _selectedPassengers.value = it
        }
    }


    private val refundQuotationMutex = Mutex()
    fun refundQuotation(
        eTickets: String?,
        bookingCode: String,
    ) {
        if(refundQuotationMutex.isLocked) {
            return
        }

        executeSuspendedCodeBlock (REFUND_QUOTATION_OPERATION_TAG) {
            refundQuotationMutex.lock()
            apiService.refundQuotation(token, eTickets, bookingCode)
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        if(operationTag == REFUND_QUOTATION_OPERATION_TAG) {
            val response = (data.body as RestResponse<*>).response
            if(response!=null) {
                liveRefundQuotationResponse.postValue(Event(response as RefundQuotationResponse))
            }
            refundQuotationMutex.unlock()
        } else if(operationTag == RefundFlightSelectionVM.RefundConfirmResponse) {
            val response = (data.body as RestResponse<*>)
            if(response!=null){
                refundConfirmResponse.postValue(true)
            }
            refundConfirmMutex.unlock()
        }
        else {
            Log.d(TAG, "Unhandled case on success. optag: $operationTag, data: $data")
        }
    }

    override fun onAnyError(operationTag: String, errorMessage: String, type: ErrorType?) {
        super.onAnyError(operationTag, errorMessage, type)
        if(operationTag == REFUND_QUOTATION_OPERATION_TAG) {
            refundQuotationMutex.unlock()
        } else if(operationTag == RefundFlightSelectionVM.RefundConfirmResponse) {
            refundConfirmMutex.unlock()
        }
    }



    private val refundConfirmMutex = Mutex()
    fun refundConfirm(refundSearchId : String){
        if(refundConfirmMutex.isLocked) {
            return
        }

        val refundSearchIdBody = RefundSearchIdBody(refundSearchId)
        executeSuspendedCodeBlock(RefundFlightSelectionVM.RefundConfirmResponse) {
            refundConfirmMutex.lock()
            apiService.confirmRefundRequest(token, refundSearchIdBody)
        }
    }

}