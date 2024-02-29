package net.sharetrip.b2b.view.flight.booking.ui.passengerlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PassengerListVMFactory(private val repo: PassengerListRepo, private val isDomestic: Boolean) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PassengerListVM::class.java))
            return PassengerListVM(repo, isDomestic) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
