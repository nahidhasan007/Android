package net.sharetrip.b2b.view.flight.history.bookingdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.view.flight.history.model.FlightHistory
import net.sharetrip.b2b.view.flight.history.model.FlightHistoryResponse

class BookingHistoryDetailsVMF(
    private val historyResponse: FlightHistory
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BookingHistoryDetailsVM(historyResponse) as T
    }

}