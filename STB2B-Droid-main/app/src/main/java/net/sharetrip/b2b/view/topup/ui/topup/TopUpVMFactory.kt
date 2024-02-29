package net.sharetrip.b2b.view.topup.ui.topup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TopUpVMFactory(private val topUpRepo: TopUpRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopUpVM::class.java))
            return TopUpVM(topUpRepo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}