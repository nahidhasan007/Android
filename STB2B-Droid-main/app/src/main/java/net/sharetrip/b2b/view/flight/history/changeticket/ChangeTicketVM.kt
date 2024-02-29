package net.sharetrip.b2b.view.flight.history.changeticket

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.*
import net.sharetrip.b2b.view.flight.history.model.FlightHistory
import net.sharetrip.b2b.view.flight.history.model.VoidDetails
import net.sharetrip.b2b.view.flight.history.model.VoidResponse

class ChangeTicketVM(
    private val repo: ChangeTicketRepo,
    var flightHistory: FlightHistory?,
    private val actionCode: Int
) :
    ViewModel() {
    val dataLoading = ObservableBoolean(false)
    var status: String? = null
    val showMessage = MutableLiveData<Event<String>>()
    val isCheckedTermsAndCon = ObservableBoolean(false)
    val isConfirmed = MutableLiveData<Event<Boolean>>()
    var moveToConfirmation = MutableLiveData<Event<Pair<Boolean, String>>>()
    var flightPolicy = ""
    val requestReason = ObservableField<String>("")

    init {
        flightHistory?.airFareRules?.forEach {
            it.policy.rules.forEach { rule ->
                if (rule.code == 16) {
                    flightPolicy = rule.text
                }
            }
        }
    }

    fun modifyTicket() {
        viewModelScope.launch {
            dataLoading.set(true)
            val selectedTravellers = flightHistory?.travellers?.filter { it.isChecked }
            val reason = if (actionCode == TICKET_ACTION_VOID) {
                null
            } else requestReason.get()
            val data = repo.modifyTicket(
                VoidDetails(
                    reason,
                    flightHistory?.bookingCode!!,
                    flightHistory?.pnrCode!!,
                    selectedTravellers!!
                ), when (actionCode) {
                    TICKET_ACTION_VOID -> {
                        VOID
                    }
                    TICKET_ACTION_REFUND -> {
                        REFUND
                    }
                    else -> {
                        TEMPORARY_CANCEL
                    }
                }
            )
            voidResponseOperation(data)
            dataLoading.set(false)
        }
    }

    private fun voidResponseOperation(data: GenericResponse<RestResponse<VoidResponse>>) {
        when (data) {
            is BaseResponse.Success -> {
                moveToConfirmation.value = Event(Pair(true, MODIFICATION))
            }
            is BaseResponse.ApiError -> {
                moveToConfirmation.value = Event(Pair(false, MODIFICATION))
            }

            is BaseResponse.NetworkError -> showMessage.value = Event(MsgUtils.networkErrorMsg)

            is BaseResponse.UnknownError -> showMessage.value = Event(MsgUtils.unKnownErrorMsg)
        }
    }
}


