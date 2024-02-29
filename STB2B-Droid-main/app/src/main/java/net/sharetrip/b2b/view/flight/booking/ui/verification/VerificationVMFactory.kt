package net.sharetrip.b2b.view.flight.booking.ui.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VerificationVMFactory(private val repo: VerificationRepo) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VerificationVM::class.java))
            return VerificationVM(repo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
