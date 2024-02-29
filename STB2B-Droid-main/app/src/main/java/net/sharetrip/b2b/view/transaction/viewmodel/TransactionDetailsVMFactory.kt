package net.sharetrip.b2b.view.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.view.transaction.view.details.TransactionDetailsRepo

class TransactionDetailsVMFactory(private val transactionDetailsRepo: TransactionDetailsRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionDetailsVM::class.java))
            return TransactionDetailsVM(transactionDetailsRepo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}