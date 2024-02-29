package net.sharetrip.b2b.view.transaction.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericError
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.view.transaction.mapper.TransactionMapper
import net.sharetrip.b2b.view.transaction.model.TransactionListData
import net.sharetrip.b2b.view.transaction.view.transactionlist.TransactionListRepo
import net.sharetrip.b2b.view.transaction.view.transactionlist.TransactionUIListData

class TransactionListVM(private val transactionListRepo: TransactionListRepo) : BaseViewModel() {
    val transactionList = MutableLiveData<Event<TransactionUIListData>>()
    var dataSize = 0

    init {
        getTransactionList(0)
    }

    fun getTransactionList(offset: Int, filter: String? = null) {
        viewModelScope.launch {
            dataLoading.set(true)
            val data = transactionListRepo.transactionList(offset, filter)
            changeTransactionResponse(data)
        }
    }

    private fun changeTransactionResponse(data: BaseResponse<RestResponse<TransactionListData>, GenericError>) {
        when (data) {
            is BaseResponse.Success -> {
                data.body.response.count?.let { it-> dataSize = it }
                transactionList.value = Event(TransactionMapper().mapListData(data.body.response))
            }
            is BaseResponse.ApiError -> _showMessage.value = Event(data.errorBody.message)

            is BaseResponse.NetworkError -> _showMessage.value = Event(MsgUtils.networkErrorMsg)

            is BaseResponse.UnknownError -> _showMessage.value = Event(MsgUtils.unKnownErrorMsg)
        }
        dataLoading.set(false)
    }
}