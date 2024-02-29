package net.sharetrip.b2b.view.topup.ui.paymentrequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PaymentRequestVMFactory(private val paymentRequestRepo: PaymentRequestRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentRequestVM::class.java))
            return PaymentRequestVM(paymentRequestRepo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}