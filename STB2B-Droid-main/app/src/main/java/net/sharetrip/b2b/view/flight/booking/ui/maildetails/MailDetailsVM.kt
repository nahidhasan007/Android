package net.sharetrip.b2b.view.flight.booking.ui.maildetails

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.EmptyResponse
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.view.flight.booking.model.FlightDetails
import net.sharetrip.b2b.view.flight.booking.model.FlightSearch
import net.sharetrip.b2b.view.flight.booking.model.Flights
import net.sharetrip.b2b.view.flight.booking.model.MailDetails

class MailDetailsVM(
    private val mailDetailsRepo: MailDetailsRepo, private val flightSearch: FlightSearch,
    private val updatedFlightList: List<Flights>, private val cancellationPolicy: Boolean,
) : ViewModel() {

    private var flightDetails = FlightDetails()
    val mailDetails = ObservableField(MailDetails())
    val dataLoading = ObservableBoolean(false)

    val showMessage: LiveData<String>
        get() = _showMessage
    private val _showMessage = MutableLiveData<String>()

    val isEmailDiscard: LiveData<Boolean>
        get() = _isEmailDiscard
    private val _isEmailDiscard = MutableLiveData<Boolean>()

    fun onSendButtonClick() {
        mailDetails.get()?.let { details ->
            val emailDetails = mailDetails.get()!!
            if (details.isValid(emailDetails.cc.isNotEmpty(), emailDetails.bcc.isNotEmpty())) {
                viewModelScope.launch {
                    dataLoading.set(true)
                    flightDetails.setMailDetails(mailDetails.get()!!,
                        flightSearch,
                        updatedFlightList, cancellationPolicy.toString())
                    val data = mailDetailsRepo.senDMail(flightDetails)
                    dataLoading.set(false)
                    mailResponseOperation(data)
                }
            } else {
                _showMessage.value = MsgUtils.inValidInputMsg
            }
        }
    }

    private fun mailResponseOperation(data: GenericResponse<RestResponse<EmptyResponse>>) {
        when (data) {
            is BaseResponse.Success ->
                _showMessage.value = data.body.message

            is BaseResponse.ApiError ->
                _showMessage.value = data.errorBody.message

            is BaseResponse.NetworkError ->
                _showMessage.value = MsgUtils.networkErrorMsg

            is BaseResponse.UnknownError ->
                _showMessage.value = MsgUtils.unKnownErrorMsg
        }
    }

    fun onDiscardButtonClick() {
        _isEmailDiscard.value = true
    }
}
