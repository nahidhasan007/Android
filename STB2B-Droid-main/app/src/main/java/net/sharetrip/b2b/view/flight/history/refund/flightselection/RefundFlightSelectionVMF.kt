package net.sharetrip.b2b.view.flight.history.refund.flightselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.view.flight.history.reissue_change_date.manualquotation.QuotationVM
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService

class RefundFlightSelectionVMF(private val apiService: FlightEndPoint, val token: String) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RefundFlightSelectionVM::class.java))
            return RefundFlightSelectionVM(apiService, token) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}