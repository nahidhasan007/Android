package net.sharetrip.b2b.view.flight.history.historydetails

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.EmptyResponse
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.*
import net.sharetrip.b2b.view.flight.history.model.*
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ErrorType
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.RefundSearchIdBody
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueEligibilityResponse
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.VoidSearchIdBody
import net.sharetrip.b2b.widgets.BaseOperationalVm

class FlightHistoryDetailsVM(
    private val apiService: FlightEndPoint,
    private val repo: FlightHistoryDetailsRepo,
    private val flightHistory: FlightHistory
) : BaseOperationalVm() {
    val isConfirmed = MutableLiveData<Event<Boolean>>()
    var moveToConfirmation = MutableLiveData<Event<Pair<Boolean, String>>>()
    val showIssueTicket = ObservableBoolean(false)
    val showCancelBooking = ObservableBoolean(false)
    val flightHistoryAction = ObservableField<FlightHistoryAction>()
    val isShowModifyButtons = ObservableBoolean(true)
    val isSHowModifyBooking = ObservableBoolean(false)
    var shouldShowVoidRefundDisabledWarning = ObservableBoolean(false)
    var reissueSearchId: String = ""
    var isDomestic = false

    val refundQuotationResponse = MutableLiveData<RefundQuotationResponse>()
    val voidQuotationResponse = MutableLiveData<VRRQuotationResponse>()
    var quotationStatus = MutableLiveData(false)

    private val _reissueEligibilityResponse = MutableLiveData<Event<ReissueEligibilityResponse>>()

    val reissueEligibilityResponse: LiveData<Event<ReissueEligibilityResponse>>
        get() = _reissueEligibilityResponse

    private val _refundEligibilityResponse = MutableLiveData<Event<RefundEligibleTravellerResponse>>()
    val refundEligibilityResponse: LiveData<Event<RefundEligibleTravellerResponse>>
        get() = _refundEligibilityResponse

    private val _voidEligibilityResponse = MutableLiveData<Event<RefundEligibleTravellerResponse>>()
    val voidEligibilityResponse: LiveData<Event<RefundEligibleTravellerResponse>>
        get() = _voidEligibilityResponse

    init {
        if (flightHistory.paymentStatus.equals(
                Status.PAID.status,
                true
            ) && ((flightHistory.bookingStatus.equals(
                Status.ISSUED.status,
                true
            ) || flightHistory.bookingStatus.equals(Status.REISSUED.status, true)))
        ) {
            viewModelScope.launch {
                dataLoading.set(true)
                val data = flightHistory.pnrCode?.let {
                    repo.getHistoryActionVisibility(
                        flightHistory.bookingCode,
                        it
                    )
                }
                data?.let { historyActionResponseOperation(it) }
                dataLoading.set(false)
            }
        }

        flightHistory.lastCancellationTime?.let {
            flightHistory.bookingStatus?.let { it1 ->
                checkIssueTicketVisibility(
                    it,
                    it1
                )
                flightHistory.paymentStatus?.let { it2 ->
                    checkCancelBookingAvailability(
                        it,
                        it1, it2
                    )
                }
            }
        }
    }

    private fun checkIssueTicketVisibility(lastCancellationTime: String, status: String) {
        try {
            if (lastCancellationTime.isNotEmpty()) {
                val cancellationTime = DateUtils.getDateFormat(
                    DateUtils.changeDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                        DateUtils.YYYY_MM_DD_HH_MM_SS,
                        lastCancellationTime
                    )
                )
                val currentTime = DateUtils.getDateFormat((DateUtils.getCurrentDateWithMin()))

                val isAvailable = cancellationTime
                    .after(currentTime) ||
                        cancellationTime == currentTime
                showIssueTicket.set(isAvailable && status.equals("BOOKED", ignoreCase = true))
            }
        } catch (e: Exception) {
        }
    }

    private fun checkCancelBookingAvailability(
        lastCancellationTime: String,
        bookingStatus: String,
        paymentStatus: String
    ) {
        try {
            if (lastCancellationTime.isNotEmpty()) {
                val cancellationTime = DateUtils.getDateFormat(
                    DateUtils.changeDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                        DateUtils.YYYY_MM_DD_HH_MM_SS,
                        lastCancellationTime
                    )
                )
                val currentTime = DateUtils.getDateFormat((DateUtils.getCurrentDateWithMin()))

                val isAvailable = cancellationTime
                    .after(currentTime) ||
                        cancellationTime == currentTime
                showCancelBooking.set(
                    isAvailable && bookingStatus.equals(
                        Status.BOOKED.status,
                        ignoreCase = true
                    ) && paymentStatus.equals(Status.UNPAID.status, ignoreCase = true)
                )
            }
        } catch (e: Exception) {
        }
    }

    fun showAllButtons() {
        isShowModifyButtons.set(false)
    }

    fun hideAllButtons() {
        isShowModifyButtons.set(true)
        showOrHideVoidRefundDisabledWarning(false)
    }

    fun issueTicket() {
        viewModelScope.launch {
            dataLoading.set(true)
            val data = repo.issueTicket(
                IssueTicketDetails(
                    flightHistory.bookingCode,
                    flightHistory.pnrCode
                )
            )
            issueTicketResponseOperation(data)
            dataLoading.set(false)
        }
    }

    fun cancelBooking() {
        viewModelScope.launch {
            dataLoading.set(true)
            val data = repo.cancelBooking(
                flightHistory.bookingCode
            )
            cancelBookingResponseOperation(data)
            dataLoading.set(false)
        }
    }

    private fun issueTicketResponseOperation(data: GenericResponse<RestResponse<IssueTicketResponse>>) {
        when (data) {
            is BaseResponse.Success -> {
                if (data.body.response.type.equals("Issued", ignoreCase = true)) {
                    moveToConfirmation.value = Event(Pair(true, ISSUE_TICKET))
                } else {
                    moveToConfirmation.value = Event(Pair(false, ISSUE_TICKET))
                }
            }
            is BaseResponse.ApiError -> _showMessage.value = Event(data.errorBody.message)

            is BaseResponse.NetworkError -> _showMessage.value = Event(MsgUtils.networkErrorMsg)

            is BaseResponse.UnknownError -> _showMessage.value = Event(MsgUtils.unKnownErrorMsg)
        }
    }

    private fun cancelBookingResponseOperation(data: GenericResponse<RestResponse<EmptyResponse>>) {
        when (data) {
            is BaseResponse.Success -> {
                moveToConfirmation.value = Event(Pair(true, CANCEL_BOOKING))
            }
            is BaseResponse.ApiError -> {
                _showMessage.value = Event(data.errorBody.message)
                moveToConfirmation.value = Event(Pair(false, CANCEL_BOOKING))
            }

            is BaseResponse.NetworkError -> _showMessage.value = Event(MsgUtils.networkErrorMsg)

            is BaseResponse.UnknownError -> _showMessage.value = Event(MsgUtils.unKnownErrorMsg)
        }
    }

    private fun historyActionResponseOperation(data: GenericResponse<RestResponse<FlightHistoryActionResponse>>) {
        when (data) {
            is BaseResponse.Success -> {
                val response = data.body.response.data
                flightHistoryAction.set(response)

                if (!response.void && !response.refund && !response.reissue && !response.temporaryCancel) {
                    isSHowModifyBooking.set(false)
                } else {
                    isSHowModifyBooking.set(true)
                }
            }
            is BaseResponse.ApiError -> _showMessage.value = Event(data.errorBody.message)

            is BaseResponse.NetworkError -> _showMessage.value = Event(MsgUtils.networkErrorMsg)

            is BaseResponse.UnknownError -> _showMessage.value = Event(MsgUtils.unKnownErrorMsg)
        }
    }

    fun showOrHideVoidRefundDisabledWarning(isShow:Boolean) {
        shouldShowVoidRefundDisabledWarning.set(isShow)
    }



    fun onClickReissueChangeDate(someBookingCode: String) {
        executeSuspendedCodeBlock(ELIGIBILITY_RESPONSE) {
            repo.checkReissueEligibility(someBookingCode)
        }
    }

    fun refundEligibleTravellers(bookingCode : String){
        executeSuspendedCodeBlock(REFUND_ELIGIBLE_TRAVELLERS) {
            repo.checkRefundEligibleTravellers(bookingCode)
        }
    }

    fun getRefundQuotation(bookingCode: String) {
        executeSuspendedCodeBlock(REFUND_QUOTATION) {
            repo.refundQuotationRequest(bookingCode)
        }
    }

    fun voidEligibleTravellers(bookingCode : String){
        executeSuspendedCodeBlock(VOID_ELIGIBLE_TRAVELLERS) {
            repo.checkVoidEligibleTravellers(bookingCode)
        }
    }

    fun getVoidQuotation(bookingCode: String) {
        executeSuspendedCodeBlock(VOID_QUOTATION) {
            apiService.voidQuotation(AppSharedPreference.accessToken, eTickets = null, bookingCode)
        }
    }

    fun cancelVoidRequest(voidSearchId: String) {
        val voidSearchIdBody = VoidSearchIdBody(voidSearchId)
        executeSuspendedCodeBlock(VOID_CANCEL) {
            apiService.cancelVoidRequest(AppSharedPreference.accessToken, voidSearchIdBody)
        }
    }

    fun cancelRefundRequest(refundSearchId: String) {
        val refundSearchIdBody = RefundSearchIdBody(refundSearchId)
        executeSuspendedCodeBlock(REFUND_CANCEL) {
            apiService.cancelRefundRequest(AppSharedPreference.accessToken, refundSearchIdBody)
        }
    }




    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        Log.e("Operationtag", operationTag)
        when(operationTag){

            ELIGIBILITY_RESPONSE -> {
                val response = (data.body as RestResponse<*>).response as ReissueEligibilityResponse
                _reissueEligibilityResponse.postValue(Event(response))
            }

            REFUND_ELIGIBLE_TRAVELLERS -> {
                val response = (data.body as RestResponse<*>).response as RefundEligibleTravellerResponse
                _refundEligibilityResponse.postValue(Event(response))
            }

            VOID_ELIGIBLE_TRAVELLERS -> {
                val response = (data.body as RestResponse<*>).response as RefundEligibleTravellerResponse
                _voidEligibilityResponse.postValue(Event(response))
            }

            REFUND_QUOTATION-> {
                val response = (data.body as RestResponse<*>).response
                Log.e("RefundQuotation", response.toString())
                if(response!=null) {
                    quotationStatus.postValue(true)
                    refundQuotationResponse.postValue(response as RefundQuotationResponse)
                }
            }
            VOID_QUOTATION -> {
                val response = (data.body as RestResponse<*>).response
                if(response!=null){
                    voidQuotationResponse.postValue(response as VRRQuotationResponse)
                }
            }
        }
    }

    override fun onAnyError(operationTag: String, errorMessage: String, type: ErrorType?) {
        super.onAnyError(operationTag, errorMessage, type)
        Log.e("Operationtag", operationTag)
        when(operationTag){
            VOID_QUOTATION -> {
                Log.e("Error!", errorMessage)
            }
        }
    }
    companion object {
        const val ELIGIBILITY_RESPONSE = "eligibility_response"
        const val REFUND_ELIGIBLE_TRAVELLERS = "REFUND_ELIGIBLE_TRAVELLERS"
        const val VOID_ELIGIBLE_TRAVELLERS = "VOID_ELIGIBLE_TRAVELLERS"
        const val REFUND_QUOTATION = "REFUND_QUOTATION"
        const val VOID_QUOTATION = "VOID_QUOTATION"
        const val VOID_CANCEL = "VOID_CANCEL"
        const val REFUND_CANCEL = "REFUND_CANCEL"
    }
}