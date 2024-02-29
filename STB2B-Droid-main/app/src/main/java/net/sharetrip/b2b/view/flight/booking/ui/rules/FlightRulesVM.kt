package net.sharetrip.b2b.view.flight.booking.ui.rules

import android.os.Bundle
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
import net.sharetrip.b2b.view.flight.booking.model.AirFareResponse
import net.sharetrip.b2b.view.flight.booking.ui.rules.FlightRulesFragment.Companion.ARG_SEARCH_ID
import net.sharetrip.b2b.view.flight.booking.ui.rules.FlightRulesFragment.Companion.ARG_SEQUENCE_CODE
import net.sharetrip.b2b.view.flight.booking.ui.rules.FlightRulesFragment.Companion.ARG_SESSION_ID

class FlightRulesVM(private val bundle: Bundle?, private val flightRulesRepo: FlightRulesRepo) :
    ViewModel() {
    val dataLoading = ObservableBoolean(false)
    val flightRules = MutableLiveData<AirFareResponse>()
    val showMessage: LiveData<String>
        get() = _showMessage
    private val _showMessage = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            dataLoading.set(true)
            val data = flightRulesRepo.getFlightRules(
                bundle?.getString(ARG_SEARCH_ID)!!,
                bundle.getString(ARG_SESSION_ID)!!,
                bundle.getString(ARG_SEQUENCE_CODE)!!
            )
            dataLoading.set(false)
            rulesResponseOperation(data)
        }
    }

    private fun rulesResponseOperation(data: GenericResponse<RestResponse<AirFareResponse>>) {
        when (data) {
            is BaseResponse.Success ->
                flightRules.value = data.body.response

            is BaseResponse.ApiError ->
                _showMessage.value = data.errorBody.message

            is BaseResponse.NetworkError ->
                _showMessage.value = MsgUtils.networkErrorMsg

            is BaseResponse.UnknownError ->
                _showMessage.value = MsgUtils.unKnownErrorMsg
        }
    }
}
