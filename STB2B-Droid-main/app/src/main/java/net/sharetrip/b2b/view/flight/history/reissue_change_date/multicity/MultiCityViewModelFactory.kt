package net.sharetrip.b2b.view.flight.history.reissue_change_date.multicity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlight

class MultiCityViewModelFactory(
    private val searchQuery: String,
    private val stringForTo: String,
    private val stringForFrom: String,
    private val stringForDate: String,
    private val selectedFlights: ArrayList<ReissueFlight>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MultiCityViewModel::class.java))
            return MultiCityViewModel(
                searchQuery,
                stringForTo,
                stringForFrom,
                stringForDate,
                selectedFlights
            ) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
