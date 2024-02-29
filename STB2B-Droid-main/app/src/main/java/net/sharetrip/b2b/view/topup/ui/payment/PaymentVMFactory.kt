package net.sharetrip.b2b.view.topup.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PaymentVMFactory(private val paymentRepo: PaymentRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentVM::class.java))
            return PaymentVM(paymentRepo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}