package net.sharetrip.b2b.view.flight.booking.ui.passenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PassengerVMFactory(private val repo: PassengerRepo, private val isDomestic: Boolean) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PassengerVM::class.java))
            return PassengerVM(repo, isDomestic) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
