package net.sharetrip.b2b.view.flight.history.changedetails

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.DateUtils
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.util.REFUND_PRICE_UPDATED
import net.sharetrip.b2b.view.flight.history.model.ChangeTicketDetails
import net.sharetrip.b2b.view.flight.history.model.FlightHistory

class ChangedFlightDetailsVM(private val repo: ChangedFlightDetailsRepo) : BaseViewModel() {

    val changedFlightDetails = ObservableField<ChangeTicketDetails>()
    val flightHistory = MutableLiveData<Event<FlightHistory>>()
    val isRefundExpired = ObservableBoolean(true)

    init {
        getTicketDetails()
    }

    private fun getTicketDetails() {
        viewModelScope.launch {
            dataLoading.set(true)
            val data = repo.exchangeRequestDetails()
            changeTicketResponse(data)
        }
    }

    private fun checkExpireTimeVisibility(expTime: String, status: String) {
        try {
            if (expTime.isNotEmpty()) {
                val expireTime = DateUtils.getDateFormat(
                    DateUtils.changeDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                        DateUtils.YYYY_MM_DD_HH_MM_SS,
                        expTime
                    )
                )
                val currentTime = DateUtils.getDateFormat((DateUtils.getCurrentDateWithMin()))

                val isAvailable = expireTime
                    .after(currentTime) ||
                        expireTime == currentTime
                isRefundExpired.set(
                    !isAvailable ||status.equals(REFUND_PRICE_UPDATED, ignoreCase = true)
                )
            }
        } catch (e: Exception) {
        }
    }

    private fun changeTicketResponse(data: GenericResponse<RestResponse<ChangeTicketDetails>>) {
        when (data) {
            is BaseResponse.Success -> {
                changedFlightDetails.set(data.body.response)
                data.body.response.expiredAt?.let { data.body.response.requestStatus?.let { it1 ->
                    checkExpireTimeVisibility(it,
                        it1
                    )
                } }
            }
            is BaseResponse.ApiError -> _showMessage.value = Event(data.errorBody.message)

            is BaseResponse.NetworkError -> _showMessage.value = Event(MsgUtils.networkErrorMsg)

            is BaseResponse.UnknownError -> _showMessage.value = Event(MsgUtils.unKnownErrorMsg)
        }
        dataLoading.set(false)
    }

    fun getActualBookingDetails() {
        viewModelScope.launch {
            dataLoading.set(true)
            val data = changedFlightDetails.get()?.actualBookingHistory?.bookingCode?.let {
                changedFlightDetails.get()?.actualBookingHistory?.pnrCode?.let { it1 ->
                    repo.getActualBookingDetails(
                        it,
                        it1
                    )
                }
            }
            data?.let { handleBookingDetails(it) }
        }
    }

    private fun handleBookingDetails(data: GenericResponse<RestResponse<FlightHistory>>) {
        when (data) {
            is BaseResponse.Success -> {
                flightHistory.value = Event(data.body.response)
            }
            is BaseResponse.ApiError -> _showMessage.value = Event(data.errorBody.message)

            is BaseResponse.NetworkError -> _showMessage.value = Event(MsgUtils.networkErrorMsg)

            is BaseResponse.UnknownError -> _showMessage.value = Event(MsgUtils.unKnownErrorMsg)
        }
        dataLoading.set(false)
    }
}