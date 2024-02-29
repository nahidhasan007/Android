package net.sharetrip.b2b.view.topup.ui.paymenthistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PaymentHistoryVMFactory(private val paymentRepo: PaymentHistoryRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentHistoryVM::class.java))
            return PaymentHistoryVM(paymentRepo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}