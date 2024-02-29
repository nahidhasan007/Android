package net.sharetrip.b2b.view.transaction.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.DateUtils
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.view.transaction.model.TransactionDetails
import net.sharetrip.b2b.view.transaction.view.details.TransactionDetailsRepo
import net.sharetrip.b2b.view.transaction.view.details.TransactionDetailsUiData

class TransactionDetailsVM(private val transactionDetailsRepo: TransactionDetailsRepo) :
    ViewModel() {
    val transactionUiData = ObservableField<TransactionDetailsUiData>()

    val dataLoading = ObservableBoolean(false)

    val showMessage: LiveData<String>
        get() = _showMessage
    private val _showMessage = MutableLiveData<String>()

    fun loadTransactionDetails(uuid: String) {
        viewModelScope.launch {
            dataLoading.set(true)
            val data = transactionDetailsRepo.transactionDetails(uuid)
            paymentHistoryResponseOperation(data)
            dataLoading.set(false)
        }
    }

    private fun paymentHistoryResponseOperation(data: GenericResponse<RestResponse<TransactionDetails>>) {
        when (data) {
            is BaseResponse.Success -> {
                val transaction = data.body.response
                transactionUiData.set(
                    TransactionDetailsUiData(
                        transaction.amount?.toBigDecimal()?.toPlainString() + debitStatus(transaction.isDebit),
                        transaction.balance?.toBigDecimal()?.toPlainString(),
                        transaction.reference,
                        transaction.source,
                        transaction.status,
                        transaction.ticketNumber,
                        transaction.type,
                        transaction.userName,
                        transaction.uuid,
                        DateUtils.changeDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",DateUtils.API_DATE_PATTERN, transaction.created_at)
                    )
                )
            }
            is BaseResponse.ApiError ->
                _showMessage.value = data.errorBody.message

            is BaseResponse.NetworkError ->
                _showMessage.value = MsgUtils.networkErrorMsg

            is BaseResponse.UnknownError ->
                _showMessage.value = MsgUtils.unKnownErrorMsg
        }
    }

    private fun debitStatus(isDebit: Boolean?): String? = if (isDebit!=null && isDebit) " (Debit)" else if(isDebit!=null && !isDebit) " (Credit)" else null
}