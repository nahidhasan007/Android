package net.sharetrip.b2b.view.transaction.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.view.transaction.view.transactionlist.TransactionListRepo

class TransactionListVMFactory(private val transactionListRepo: TransactionListRepo) :
    ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TransactionListVM::class.java))
            return TransactionListVM(transactionListRepo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}