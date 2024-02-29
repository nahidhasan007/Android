package net.sharetrip.b2b.view.flight.booking.ui.nationality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NationalityListVMFactory(private val repo: NationalityListRepo) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NationalityListVM::class.java))
            return NationalityListVM(repo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
