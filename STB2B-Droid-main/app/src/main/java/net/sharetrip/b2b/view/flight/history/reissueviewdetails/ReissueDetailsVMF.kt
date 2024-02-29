package net.sharetrip.b2b.view.flight.history.reissueviewdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.view.flight.history.model.ModifyHistory
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService

class ReissueDetailsVMF
    (private val modifyHistory: ModifyHistory,
     private val token : String) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReissueDetailsVm::class.java))
            return ReissueDetailsVm(modifyHistory, token) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}