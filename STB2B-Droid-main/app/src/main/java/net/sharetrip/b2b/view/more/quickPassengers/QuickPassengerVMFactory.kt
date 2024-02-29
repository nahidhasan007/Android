package net.sharetrip.b2b.view.more.quickPassengers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class QuickPassengerVMFactory (private val repo: QuickPassengerRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuickPassengerVM::class.java))
            return QuickPassengerVM(repo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}