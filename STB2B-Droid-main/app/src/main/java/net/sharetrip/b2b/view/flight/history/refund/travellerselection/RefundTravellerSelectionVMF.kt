package net.sharetrip.b2b.view.flight.history.refund.travellerselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.view.flight.history.refund.flightselection.RefundFlightSelectionVM

class RefundTravellerSelectionVMF(
    private val token: String,
    private val apiService: FlightEndPoint
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RefundTravellerSelectionVM::class.java))
            return RefundTravellerSelectionVM(token, apiService) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}