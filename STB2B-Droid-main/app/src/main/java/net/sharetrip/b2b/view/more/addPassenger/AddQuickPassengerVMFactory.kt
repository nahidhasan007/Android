package net.sharetrip.b2b.view.more.addPassenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddQuickPassengerVMFactory (private val repo: AddQuickPassengerRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddQuickPassengerVM::class.java))
            return AddQuickPassengerVM(repo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}