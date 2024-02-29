package net.sharetrip.b2b.view.flight.booking.ui.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FlightFilterVMFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightFilterVM::class.java))
            return FlightFilterVM() as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
