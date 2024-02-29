package net.sharetrip.b2b.view.flight.booking.ui.rules

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FlightRulesVMFactory(private val bundle: Bundle?, private val repo: FlightRulesRepo) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightRulesVM::class.java))
            return FlightRulesVM(bundle, repo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}