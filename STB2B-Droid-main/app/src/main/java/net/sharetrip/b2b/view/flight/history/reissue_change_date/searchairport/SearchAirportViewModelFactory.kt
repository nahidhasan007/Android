package net.sharetrip.b2b.view.flight.history.reissue_change_date.searchairport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchAirportViewModelFactory(val repo: SearchAirportRepo) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchAirportViewModel::class.java))
            return SearchAirportViewModel(repo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
