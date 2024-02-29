package net.sharetrip.b2b.view.flight.history.vrrvoid.travellerselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.network.FlightEndPoint

class TravellerSelectionVMF (private val token: String, private val apiService: FlightEndPoint) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravellerSelectionVM::class.java))
            return TravellerSelectionVM(token, apiService) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}