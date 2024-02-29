package net.sharetrip.b2b.view.flight.history.historydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.view.flight.history.model.FlightHistory

class FlightHistoryDetailsVMFactory(
    private val apiService: FlightEndPoint,
    private val repo: FlightHistoryDetailsRepo,
    private val flightHistory: FlightHistory
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightHistoryDetailsVM::class.java))
            return FlightHistoryDetailsVM(
                apiService,
                repo,
                flightHistory
            ) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}