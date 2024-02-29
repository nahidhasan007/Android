package net.sharetrip.b2b.view.flight.history.reissue_change_date.flightlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.*
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService

class FlightListViewModelFactory(
    private val apiService: ReissueApiService,
    private val flightSearch: ReissueFlightSearch,
    private val repository: FlightListRepository,
    val quotationRequestBody: ReissueQuotationRequestBody,
    val token: String,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightListViewModel::class.java))
            return FlightListViewModel(apiService,flightSearch, repository,quotationRequestBody,token) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

