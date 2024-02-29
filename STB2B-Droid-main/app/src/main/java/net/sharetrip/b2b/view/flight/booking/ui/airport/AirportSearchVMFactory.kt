package net.sharetrip.b2b.view.flight.booking.ui.airport

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AirportSearchVMFactory(private val bundle: Bundle?, private val repo: AirportSearchRepo) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AirportSearchVM::class.java))
            return AirportSearchVM(bundle, repo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
