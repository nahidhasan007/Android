package net.sharetrip.b2b.view.flight.history.changelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChangedFlightHistoryListVMFactory(
    private val repo: ChangedFlightHistoryListRepo
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangedFlightHistoryListVM::class.java))
            return ChangedFlightHistoryListVM(repo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}