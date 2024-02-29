package net.sharetrip.b2b.view.flight.history.reissue_change_date.travellernumber

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.view.flight.booking.model.TravellersInfo


class TravellerNumberViewModelFactory(val travellersInfo: TravellersInfo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TravellerNumberViewModel::class.java))
            return TravellerNumberViewModel(
                travellersInfo
            ) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
