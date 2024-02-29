package net.sharetrip.b2b.view.flight.history.changeticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.view.flight.history.model.FlightHistory

class ChangeTicketVMFactory(
    private val repo: ChangeTicketRepo,
    private val flightHistory: FlightHistory?,
    private val actionCode:Int
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangeTicketVM::class.java))
            return ChangeTicketVM(repo, flightHistory,actionCode) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}