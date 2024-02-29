package net.sharetrip.b2b.view.flight.history.faredetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.databinding.FragmentFareDetailsBinding
import net.sharetrip.b2b.view.flight.history.model.PriceDetails
import net.sharetrip.b2b.widgets.ItemCommonPriceBreakDownView

class FragmentFareDetails : Fragment() {
    lateinit var bindingView: FragmentFareDetailsBinding
    var isNavigateFromFlightDetails = false
    var priceDetails: PriceDetails? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView = FragmentFareDetailsBinding.inflate(layoutInflater, container, false)
        priceDetails = arguments?.getParcelable(ARGS_PRICE_BREAKDOWN)!!
        isNavigateFromFlightDetails =
            arguments?.getBoolean(ARGS_NAVIGATE_FROM_FLIGHT_DETAILS, false)!!

        commonPriceBreakdown()

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return bindingView.root
    }

    private fun commonPriceBreakdown() {
        priceDetails?.let {
            val view = ItemCommonPriceBreakDownView(requireContext())
            view.setPriceBreakDown(it, isNavigateFromFlightDetails)
            bindingView.layoutCommonPriceContainer.addView(view)
        }
    }

    companion object {
        const val ARGS_PRICE_BREAKDOWN = "price_breakdown"
        const val ARGS_NAVIGATE_FROM_FLIGHT_DETAILS = "navigate_from_flight_details"
    }
}