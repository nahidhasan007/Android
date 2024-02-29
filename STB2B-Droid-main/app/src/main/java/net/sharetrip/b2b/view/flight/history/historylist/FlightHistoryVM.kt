package net.sharetrip.b2b.view.flight.history.historylist

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
import net.sharetrip.b2b.view.flight.history.model.FlightHistory
import net.sharetrip.b2b.view.flight.history.model.FlightHistoryResponse

class FlightHistoryVM(private val repo: FlightHistoryRepo, private val pageNo: Int) : ViewModel() {
    val flightHistoryList = MutableLiveData<FlightHistoryResponse>()
    val dataLoading = ObservableBoolean(true)

    var status: String? = null

    val showMessage: LiveData<String>
        get() = _showMessage
    private val _showMessage = MutableLiveData<String>()

    init {
        setBookingStatus()
        getFlightHistory(0)
    }

    fun getFlightHistory(offset: Int) {
        viewModelScope.launch {
            dataLoading.set(true)
            val data = repo.getFlightHistoryList(offset, status)
            historyResponseOperation(data)
            dataLoading.set(false)
        }
    }

    private fun setBookingStatus() {
        if (pageNo == 1) {
            status = "Issued"
        } else if (pageNo == 2) {
            status = "Booked"
        }
    }

    private fun historyResponseOperation(response: GenericResponse<RestResponse<FlightHistoryResponse>>) {
        when (response) {
            is BaseResponse.Success -> {
                response.body.response.data.map { flights ->
                    flights.segments?.map { segments ->
                        segments.segment?.map {
                            it.classType = flights.searchParams?.class_!!
                        }
                    }
                }
                flightHistoryList.value = response.body.response
            }
            is BaseResponse.ApiError -> _showMessage.value = response.errorBody.message

            is BaseResponse.NetworkError -> _showMessage.value = MsgUtils.networkErrorMsg

            is BaseResponse.UnknownError -> _showMessage.value = MsgUtils.unKnownErrorMsg
        }
    }
}