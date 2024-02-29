package net.sharetrip.b2b.view.flight.booking.ui.flightdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.view.flight.booking.model.FlightSearch

class FlightDetailsVM(private val flightDetailsRepo: FlightDetailsRepo, val flightSearch: FlightSearch) : ViewModel() {

    fun onContinueButtonClick() {
        viewModelScope.launch {
           flightDetailsRepo.saveFlightSearch(flightSearch)
        }
    }
}
