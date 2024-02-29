package net.sharetrip.b2b.view.flight.history.reissue_change_date.pricebreakdown

import android.view.View
import androidx.fragment.app.activityViewModels
import net.sharetrip.b2b.R
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.databinding.LayoutPriceBreakdownBinding
import net.sharetrip.b2b.util.convertCurrencyToBengaliFormat
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ReissueChangeDateSharedViewModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.flightdetails.ReissueFlightDetailsFragment.Companion.PRICE_BREAKDOWN
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.PriceBreakdown
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.getParcelableCompat
import net.sharetrip.b2b.widgets.BaseFragment

class ReissuePriceBreakDownFragment : BaseFragment<LayoutPriceBreakdownBinding>() {

    private val sharedViewModel: ReissueChangeDateSharedViewModel by activityViewModels()
    override fun layoutId(): Int {
        return R.layout.layout_price_breakdown
    }

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun initOnCreateView() {

        sharedViewModel.subtitle.postValue("Step 3:Price Information")

        val priceBreakdown: PriceBreakdown =
            requireArguments().getParcelableCompat(PRICE_BREAKDOWN, PriceBreakdown::class.java)!!

//        val stConvenienceFee: Double = priceBreakdown.stFee ?: 0.0
        if (sharedViewModel.reissueEligibilityResponse?.automationSupported == true) {
            bindingView.reissueCardViewQuoted.visibility = View.GONE
            bindingView.fareDiffPrice.text =
                "BDT ${priceBreakdown.fareDifference?.toLong()?.convertCurrencyToBengaliFormat()}"
            bindingView.airlineFeePrice.text =
                "BDT ${priceBreakdown.airlinesFee?.toLong()?.convertCurrencyToBengaliFormat()}"
            bindingView.convenienceFeePrice.text =
                "BDT ${priceBreakdown.stFee?.toLong()?.convertCurrencyToBengaliFormat()}"
            bindingView.totalAirfarePrice.text =
                "BDT ${priceBreakdown.total?.toLong()?.convertCurrencyToBengaliFormat()}"
        }


    }
}