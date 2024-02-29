package net.sharetrip.b2b.view.flight.history.refundDetails

import androidx.databinding.ObservableBoolean
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
import net.sharetrip.b2b.view.flight.history.model.RefundRequest

class ChangedFlightRefundDetailsVM(private val repo: ChangedFlightRefundDetailsRepo) :
    BaseViewModel() {
    val isRefundExpired = ObservableBoolean(true)

    val reqResult = MutableLiveData<String>()
    fun updateRefundRequest(refundDetail: RefundRequest) {
        viewModelScope.launch {
            dataLoading.set(true)
            val data = repo.ticketRequestUpdate(refundDetail)
            ticketRequestResponse(data)
        }
    }

    fun checkExpireTimeVisibility(expTime: String, status: String) {
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
                    !isAvailable || status.equals(REFUND_PRICE_UPDATED, ignoreCase = true)
                )
            }
        } catch (e: Exception) {
        }
    }

    private fun ticketRequestResponse(data: GenericResponse<RestResponse<ChangeTicketDetails>>) {
        when (data) {
            is BaseResponse.Success -> {
                reqResult.value = data.body.code
            }
            is BaseResponse.ApiError -> _showMessage.value = Event(data.errorBody.message)

            is BaseResponse.NetworkError -> _showMessage.value = Event(MsgUtils.networkErrorMsg)

            is BaseResponse.UnknownError -> _showMessage.value = Event(MsgUtils.unKnownErrorMsg)
        }
        dataLoading.set(false)
    }
}