package net.sharetrip.b2b.view.flight.history.changedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChangedFlightDetailsVMFactory(
    private val repo: ChangedFlightDetailsRepo
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangedFlightDetailsVM::class.java))
            return ChangedFlightDetailsVM(repo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}