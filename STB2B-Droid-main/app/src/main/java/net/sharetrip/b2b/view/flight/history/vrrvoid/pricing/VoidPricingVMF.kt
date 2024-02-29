package net.sharetrip.b2b.view.flight.history.vrrvoid.pricing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.network.FlightEndPoint

class VoidPricingVMF(private val token : String, private val apiService: FlightEndPoint) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoidPricingViewModel::class.java))
            return VoidPricingViewModel(token, apiService) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}