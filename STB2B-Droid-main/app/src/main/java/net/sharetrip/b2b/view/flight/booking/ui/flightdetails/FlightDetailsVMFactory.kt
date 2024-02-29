package net.sharetrip.b2b.view.flight.booking.ui.flightdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.view.flight.booking.model.FlightSearch

class FlightDetailsVMFactory (private val repo: FlightDetailsRepo,val flightSearch: FlightSearch) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightDetailsVM::class.java))
            return FlightDetailsVM(repo,flightSearch) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}