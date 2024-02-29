package net.sharetrip.b2b.view.topup.ui.payment

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableDouble
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
import net.sharetrip.b2b.view.topup.model.AgencyBalance
import net.sharetrip.b2b.view.topup.model.PaymentHistoryData

class PaymentVM(paymentRepo: PaymentRepo) : ViewModel() {
    val paymentHistoryList = MutableLiveData<Event<PaymentHistoryData>>()
    val dataLoading = ObservableBoolean(false)
    val balance = ObservableDouble()

    val showMessage: LiveData<Event<String>>
        get() = _showMessage
    private val _showMessage = MutableLiveData<Event<String>>()

    init {
        viewModelScope.launch {
            dataLoading.set(true)
            val balance = paymentRepo.checkBalance()
            balanceResponseOperation(balance)

            val data = paymentRepo.getPaymentHistory()
            paymentHistoryResponseOperation(data)
            dataLoading.set(false)
        }
    }

    private fun paymentHistoryResponseOperation(data: GenericResponse<RestResponse<PaymentHistoryData>>) {
        when (data) {
            is BaseResponse.Success ->
                paymentHistoryList.value = Event(data.body.response)

            is BaseResponse.ApiError ->
                _showMessage.value = Event(data.errorBody.message)

            is BaseResponse.NetworkError ->
                _showMessage.value = Event(MsgUtils.networkErrorMsg)

            is BaseResponse.UnknownError ->
                _showMessage.value = Event(MsgUtils.unKnownErrorMsg)
        }
    }

    private fun balanceResponseOperation(data: GenericResponse<RestResponse<AgencyBalance>>) {
        when (data) {
            is BaseResponse.Success ->
                balance.set(data.body.response.balance)

            is BaseResponse.ApiError ->
                _showMessage.value = Event(data.errorBody.message)

            is BaseResponse.NetworkError ->
                _showMessage.value = Event(MsgUtils.networkErrorMsg)

            is BaseResponse.UnknownError ->
                _showMessage.value = Event(MsgUtils.unKnownErrorMsg)
        }
    }
}
