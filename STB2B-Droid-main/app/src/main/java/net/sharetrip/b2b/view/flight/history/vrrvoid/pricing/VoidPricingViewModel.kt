package net.sharetrip.b2b.view.flight.history.vrrvoid.pricing

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ErrorType
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.VoidSearchIdBody
import net.sharetrip.b2b.widgets.BaseOperationalVm

class VoidPricingViewModel(private val token: String, private val apiService: FlightEndPoint) : BaseOperationalVm(){


    val voidConfirmMsg = MutableLiveData<Event<Boolean>>()
    val voidCancelMsg = MutableLiveData<Event<Boolean>>()

    val isLoading = ObservableBoolean()


    fun confirmVoid(voidSearchId : String){
        if (!isLoading.get()) {
            isLoading.set(true)
            executeSuspendedCodeBlock(VOID_CONFIRM) {
                val voidConfirmBody = VoidSearchIdBody(voidSearchId)
                apiService.confirmVoidRequest(token, voidConfirmBody)
            }
        }

    }

    fun cancelVoid(voidSearchId : String){
        executeSuspendedCodeBlock(VOID_CONFIRM) {
            val voidCancelBody = VoidSearchIdBody(voidSearchId)
            apiService.cancelVoidRequest(token, voidCancelBody)
        }
    }


    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        isLoading.set(false)
        Log.e("operationTag", operationTag)
       when(operationTag){
           VOID_CONFIRM -> {
               voidConfirmMsg.postValue(Event(true))
           }
           VOID_CANCEL -> {
               voidCancelMsg.postValue(Event(true))
           }
       }
    }

    override fun onAnyError(operationTag: String, errorMessage: String, type: ErrorType?) {
        super.onAnyError(operationTag, errorMessage, type)
        when(operationTag){
            VOID_CONFIRM -> {
                Log.e("Error", errorMessage)
                }
            }
        }
    companion object {
        const val VOID_CONFIRM = "VOID_CONFIRM"
        const val VOID_CANCEL = "VOID_CANCEL"
    }
}