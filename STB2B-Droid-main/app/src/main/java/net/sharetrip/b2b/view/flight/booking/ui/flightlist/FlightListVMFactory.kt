package net.sharetrip.b2b.view.flight.booking.ui.flightlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FlightListVMFactory(private val repo: FlightListRepo) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightListVM::class.java))
            return FlightListVM(repo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}