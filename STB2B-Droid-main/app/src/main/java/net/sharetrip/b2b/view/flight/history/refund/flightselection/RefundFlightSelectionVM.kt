package net.sharetrip.b2b.view.flight.history.refund.flightselection

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.sync.Mutex
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.view.flight.history.historydetails.FlightHistoryDetailsVM
import net.sharetrip.b2b.view.flight.history.model.Flight
import net.sharetrip.b2b.view.flight.history.model.RefundQuotationResponse
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ErrorType
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.RefundSearchIdBody
import net.sharetrip.b2b.widgets.BaseOperationalVm

class RefundFlightSelectionVM(private val apiService: FlightEndPoint, private val token: String) :
    BaseOperationalVm() {


    private val _refundQuotationResponse = MutableLiveData<Event<RefundQuotationResponse>>()

    val refundQuotationResponse: LiveData<Event<RefundQuotationResponse>>
        get() = _refundQuotationResponse

    var refundConfirmResponse = MutableLiveData(false)

    private val _selectedFlights = MutableLiveData<ArrayList<Flight>>(arrayListOf())
    val selectedFlights: LiveData<ArrayList<Flight>>
        get() = _selectedFlights

    val cancelRefund = MutableLiveData<Event<Boolean>>()

    val isLoading = MutableLiveData(false)

    fun addSelectedFlights(flight: Flight) {
        val temp = _selectedFlights.value
        temp?.let { flightArrayList ->
            flightArrayList.add(flight)
            flightArrayList.sortBy { flight1 ->
                flight1.departure?.dateTime
            }
            _selectedFlights.value = flightArrayList
        }
    }

    fun removeFlight(flight: Flight) {
        val temp = _selectedFlights.value
        temp?.let {
            it.remove(flight)
            _selectedFlights.value = it
        }
    }

    fun getRefundQuotation(eTickets: String, bookingCode: String) {
        executeSuspendedCodeBlock(RefundQuotationResponse) {
            apiService.refundQuotation(token, eTickets.toString().replace(" ", ""), bookingCode)
        }
    }

    fun cancelRefundRequest(refundSearchId: String) {
        val refundSearchIdBody = RefundSearchIdBody(refundSearchId)
        executeSuspendedCodeBlock(RefundCancel) {
            apiService.cancelRefundRequest(AppSharedPreference.accessToken, refundSearchIdBody)
        }
    }


    private val refundConfirmMutex = Mutex()
    fun refundConfirm(refundSearchId : String){
        if(refundConfirmMutex.isLocked) {
            return
        }
        if(isLoading.value==false) {
            isLoading.postValue(true)
            val refundSearchIdBody = RefundSearchIdBody(refundSearchId)
            executeSuspendedCodeBlock(RefundConfirmResponse) {
                refundConfirmMutex.lock()
                apiService.confirmRefundRequest(token, refundSearchIdBody)
            }
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when(operationTag){
            RefundQuotationResponse-> {
                val response = (data.body as RestResponse<*>).response
                _refundQuotationResponse.postValue(Event(response as RefundQuotationResponse))
            }
            RefundConfirmResponse-> {
               isLoading.postValue(false)
                val response = (data.body as RestResponse<*>)
                if(response!=null){
                    refundConfirmResponse.postValue(true)
                }
                refundConfirmMutex.unlock()
            }
            RefundCancel -> {
                val response = (data.body as RestResponse<*>)
                if(response!=null){
                    cancelRefund.postValue(Event(true))
                }
            }

        }
    }

    override fun onAnyError(operationTag: String, errorMessage: String, type: ErrorType?) {
        super.onAnyError(operationTag, errorMessage, type)
        if(operationTag == RefundConfirmResponse) {
            refundConfirmMutex.unlock()
        }
    }

    companion object {
        const val RefundQuotationResponse = "RefundQuotationResponse"
        const val RefundConfirmResponse = "RefundConfirmResponse"
        const val RefundCancel = "RefundCancel"
    }
}