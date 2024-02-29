package net.sharetrip.b2b.view.topup.ui.topup

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.view.topup.model.GateWay
import net.sharetrip.b2b.view.topup.model.TopUp
import net.sharetrip.b2b.view.topup.model.TopUpResponse

class TopUpVM(private val topUpRepo: TopUpRepo) : ViewModel() {
    private val topUp: TopUp = TopUp()
    val dataLoading = ObservableBoolean(false)
    val gateWays = MutableLiveData<Event<List<GateWay>>>()
    val gateWay = ObservableField<GateWay>()
    val finalAmount = ObservableDouble()
    val addedAmount = ObservableBoolean(false)
    val gateWayCharge = ObservableDouble()
    val isConfirmed = ObservableBoolean(false)

    val paymentUrl: LiveData<Event<String>>
        get() = _paymentUrl
    private val _paymentUrl = MutableLiveData<Event<String>>()

    val showMessage: LiveData<String>
        get() = _showMessage
    private val _showMessage = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            dataLoading.set(true)
            val data = topUpRepo.getPaymentGateWay()
            gateWayResponseOperation(data)
            dataLoading.set(false)
        }
    }

    private fun gateWayResponseOperation(data: GenericResponse<RestResponse<List<GateWay>>>) {
        when (data) {
            is BaseResponse.Success ->
                gateWays.value = Event(data.body.response)

            is BaseResponse.ApiError ->
                _showMessage.value = data.errorBody.message

            is BaseResponse.NetworkError ->
                _showMessage.value = MsgUtils.networkErrorMsg

            is BaseResponse.UnknownError ->
                _showMessage.value = MsgUtils.unKnownErrorMsg
        }
    }

    fun setGateWayDetails(gateWay: GateWay) {
        this.gateWay.set(gateWay)
        topUp.gateway = gateWay.id
    }

    fun setTotalAmount(inputAmount: CharSequence) {
        if (gateWay.get() != null) {
            var amount: Double
            if (inputAmount.isNotEmpty()) {
                try {
                    amount = inputAmount.toString().toDouble()
                    gateWayCharge.set(
                        "%.2f".format(amount * (gateWay.get()?.charge?.div(100)!!)).toDouble()
                    )
                } catch (e:Exception) {
                    amount = 0.0
                    gateWayCharge.set(0.0)
                    _showMessage.value = MsgUtils.numberFormatExceptionMsg
                }
            } else {
                amount = 0.0
                gateWayCharge.set(0.0)
            }

            finalAmount.set(amount - gateWayCharge.get())
            topUp.amount = amount
            addedAmount.set(inputAmount.isNotEmpty())
        }
    }

    fun clickOnContinue() {
        viewModelScope.launch {
            val data = topUpRepo.topUp(topUp)
            operationOnTopUpData(data)
        }
    }

    private fun operationOnTopUpData(data: GenericResponse<RestResponse<TopUpResponse>>) {
        when (data) {
            is BaseResponse.Success ->
                _paymentUrl.value = Event(data.body.response.url)

            is BaseResponse.ApiError ->
                _showMessage.value = data.errorBody.message

            is BaseResponse.NetworkError ->
                _showMessage.value = MsgUtils.networkErrorMsg

            is BaseResponse.UnknownError ->
                _showMessage.value = MsgUtils.unKnownErrorMsg
        }
    }

}
