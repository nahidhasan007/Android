package net.sharetrip.b2b.view.flight.booking.ui.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.view.flight.booking.model.ContactInfo

class BookingSummaryVMFactory(
    private val repo: BookingSummaryRepo,
    private val checkBalance: Boolean,
    private val contactInfo: ContactInfo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingSummaryVM::class.java))
            return BookingSummaryVM(repo, checkBalance, contactInfo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
