package net.sharetrip.b2b.view.flight.booking.ui.proposal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FlightProposalVMFactory(private val repo: FlightProposalRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightProposalViewModel::class.java))
            return FlightProposalViewModel(repo) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}