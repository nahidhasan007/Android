package net.sharetrip.b2b.view.flight.booking.ui.travellers

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TravellersVMFactory(private val bundle: Bundle?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravellersVM::class.java))
            return TravellersVM(bundle) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
