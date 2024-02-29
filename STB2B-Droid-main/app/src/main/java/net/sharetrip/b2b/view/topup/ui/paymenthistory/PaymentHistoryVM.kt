package net.sharetrip.b2b.view.topup.ui.paymenthistory

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.view.topup.model.PaymentHistory
import net.sharetrip.b2b.view.topup.model.PaymentHistoryData

class PaymentHistoryVM(private val paymentRepo: PaymentHistoryRepo) : ViewModel() {
    val paymentHistoryList = MutableLiveData<PaymentHistoryData>()
    val dataLoading = ObservableBoolean(false)

    val showMessage: LiveData<String>
        get() = _showMessage
    private val _showMessage = MutableLiveData<String>()

    init {
        loadPaymentHistory(0)
    }

    fun loadPaymentHistory(currentPage: Int) {
        viewModelScope.launch {
            dataLoading.set(true)

            val data = paymentRepo.getPaymentHistory(currentPage)
            paymentHistoryResponseOperation(data)

            dataLoading.set(false)
        }
    }

    private fun paymentHistoryResponseOperation(data: GenericResponse<RestResponse<PaymentHistoryData>>) {
        when (data) {
            is BaseResponse.Success ->
                paymentHistoryList.value = data.body.response

            is BaseResponse.ApiError ->
                _showMessage.value = data.errorBody.message

            is BaseResponse.NetworkError ->
                _showMessage.value = MsgUtils.networkErrorMsg

            is BaseResponse.UnknownError ->
                _showMessage.value = MsgUtils.unKnownErrorMsg
        }
    }

}