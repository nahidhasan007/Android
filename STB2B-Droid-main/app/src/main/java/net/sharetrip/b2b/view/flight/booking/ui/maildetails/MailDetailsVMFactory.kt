package net.sharetrip.b2b.view.flight.booking.ui.maildetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.view.flight.booking.model.FlightSearch
import net.sharetrip.b2b.view.flight.booking.model.Flights

class MailDetailsVMFactory(
    private val repo: MailDetailsRepo, private val flightSearch: FlightSearch,
    private val updatedFlightList: List<Flights>, private val cancellationPolicy: Boolean,
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MailDetailsVM::class.java))
            return MailDetailsVM(repo, flightSearch, updatedFlightList, cancellationPolicy) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
