package net.sharetrip.b2b.view.flight.history.changelist

import androidx.databinding.ObservableDouble
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.view.flight.history.model.ChangeTicketListResponse

class ChangedFlightHistoryListVM(private val repo: ChangedFlightHistoryListRepo) : BaseViewModel() {
    val changedFlightHistoryDetailsList = MutableLiveData<Event<ChangeTicketListResponse>>()
    val dataSize = ObservableDouble()

    init {
        getChangeTicketList(0)
    }

    fun getChangeTicketList(offset: Int, filter: String? = null) {
        viewModelScope.launch {
            dataLoading.set(true)
            val data = repo.exchangeRequest(offset, filter)
            changeTicketResponse(data)
        }
    }

    private fun changeTicketResponse(data: GenericResponse<RestResponse<ChangeTicketListResponse>>) {
        when (data) {
            is BaseResponse.Success -> {
                dataSize.set(data.body.response.data?.size!!.toDouble())
                changedFlightHistoryDetailsList.value = Event(data.body.response)
            }
            is BaseResponse.ApiError -> _showMessage.value = Event(data.errorBody.message)

            is BaseResponse.NetworkError -> _showMessage.value = Event(MsgUtils.networkErrorMsg)

            is BaseResponse.UnknownError -> _showMessage.value = Event(MsgUtils.unKnownErrorMsg)
        }
        dataLoading.set(false)
    }
}