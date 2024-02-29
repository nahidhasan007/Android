package net.sharetrip.b2b.view.flight.history.refundDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChangedFlightRefundDetailsVMFactory(
    private val repo: ChangedFlightRefundDetailsRepo
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangedFlightRefundDetailsVM::class.java))
            return ChangedFlightRefundDetailsVM(repo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}