package net.sharetrip.b2b.view.flight.history.historylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FlightHistoryVMFactory(private val repo: FlightHistoryRepo, private val pageNo: Int) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightHistoryVM::class.java))
            return FlightHistoryVM(repo, pageNo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}