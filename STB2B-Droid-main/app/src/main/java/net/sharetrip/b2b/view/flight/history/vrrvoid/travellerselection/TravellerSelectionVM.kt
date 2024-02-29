package net.sharetrip.b2b.view.flight.history.vrrvoid.travellerselection

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.view.flight.history.model.VRRQuotationResponse
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ErrorType
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.VoidSearchIdBody
import net.sharetrip.b2b.widgets.BaseOperationalVm

class TravellerSelectionVM(
    private val token: String,
    private val apiService: FlightEndPoint
) : BaseOperationalVm() {

    private val _selectedPassengers = MutableLiveData<ArrayList<ReissueTraveller>>(arrayListOf())
    val selectedPassengers: LiveData<ArrayList<ReissueTraveller>>
        get() = _selectedPassengers

    val voidQuotationResponse = MutableLiveData<Event<VRRQuotationResponse>>()
    fun addSelectedTraveller(traveller: ReissueTraveller) {
        val temp = _selectedPassengers.value
        temp?.let {
            it.add(traveller)
            _selectedPassengers.value = it
        }
    }
    val voidStatus = MutableLiveData(false)

    fun removeTraveller(traveller: ReissueTraveller) {
        val temp = _selectedPassengers.value
        temp?.let {
            it.remove(traveller)
            _selectedPassengers.value = it
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        Log.e("OperationTag", operationTag)
        when (operationTag) {
            VoidQuotationResponse -> {
                val response = (data.body as RestResponse<*>).response
                Log.e("response", response.toString())
                if (response != null) {
                    voidQuotationResponse.postValue(Event(response as VRRQuotationResponse))
                }
            }
            VOID_CONFIRM -> {
                val response = (data.body as RestResponse<*>).response
                if(response!=null){
                    voidStatus.postValue(true)
                }
            }
        }
    }

    override fun onAnyError(operationTag: String, errorMessage: String, type: ErrorType?) {
        Log.e("tag", operationTag)
        super.onAnyError(operationTag, errorMessage, type)
        when(operationTag){
            VoidQuotationResponse -> {
                Log.e("Error!", errorMessage)
            }
        }
    }

    fun getVoidQuotation(eTickets: String, bookingCode: String) {
        executeSuspendedCodeBlock(VoidQuotationResponse) {
            apiService.voidQuotation(token, eTickets.toString().replace(" ", ""), bookingCode)
        }
    }

    fun confirmVoid(voidSearchId : String){
        executeSuspendedCodeBlock(VOID_CONFIRM) {
            val voidConfirmBody = VoidSearchIdBody(voidSearchId)
            apiService.confirmVoidRequest(token, voidConfirmBody)
        }
    }

//    suspend fun checkVoidEligibleTravellers(bookingCode: String): GenericResponse<RestResponse<RefundEligibleTravellerResponse>> {
//        return try {
//            val accessToken = AppSharedPreference.accessToken
//            val output = flightEndPoint.voidEligibleTravellers(key = accessToken, bookingCode = bookingCode)
//
//            Log.d("NahidDebug123", "inside normal: ${output.toString()}")
//            output
//        } catch (x: Exception) {
//            x.printStackTrace()
//            Log.d("NahidDebug123", "inside crash: ${x.message}")
//            BaseResponse.UnknownError(x)
//        }
//    }

    companion object {
        const val VoidQuotationResponse = "VoidQuotationResponse"
        const val VOID_CONFIRM = "VOID_CONFIRM"
    }
}