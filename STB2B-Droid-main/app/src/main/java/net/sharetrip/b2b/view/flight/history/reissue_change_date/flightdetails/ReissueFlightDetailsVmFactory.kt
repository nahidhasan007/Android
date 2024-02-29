package net.sharetrip.b2b.view.flight.history.reissue_change_date.flightdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlightSearch
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService

class ReissueFlightDetailsVMFactory(val flightSearch: ReissueFlightSearch, private val apiService: ReissueApiService, val token: String) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReissueFlightDetailsVm::class.java))
            return ReissueFlightDetailsVm(flightSearch, apiService, token) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
