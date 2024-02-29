package net.sharetrip.b2b.view.flight.booking.ui.segment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.view.flight.booking.model.Flights
import net.sharetrip.b2b.view.flight.history.model.FlightHistory

class FlightSegmentVmFactory(private val flights: Flights?, private val flightHistory: FlightHistory?) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightSegmentVM::class.java))
            return FlightSegmentVM(flights, flightHistory) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
