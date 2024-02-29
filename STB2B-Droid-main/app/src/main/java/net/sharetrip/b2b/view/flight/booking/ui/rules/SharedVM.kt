package net.sharetrip.b2b.view.flight.booking.ui.rules

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.sharetrip.b2b.view.flight.booking.model.AirFareResponse

class SharedVM : ViewModel() {
    val airFareRules = MutableLiveData<Any>()

    fun setAirFareResponse(airFareResponse: AirFareResponse) {
        airFareRules.value = airFareResponse
    }

    fun clearVM() {
        onCleared()
    }

    override fun onCleared() {
        airFareRules.value = ""
        super.onCleared()
    }
}
