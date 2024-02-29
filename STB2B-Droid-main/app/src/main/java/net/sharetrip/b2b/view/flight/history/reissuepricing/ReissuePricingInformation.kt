package net.sharetrip.b2b.view.flight.history.reissuepricing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.sharetrip.b2b.databinding.ViewPriceDetailsBinding
import net.sharetrip.b2b.view.flight.history.model.PriceBreakdown
import net.sharetrip.b2b.view.flight.history.reissue_change_date.flightdetails.ReissueFlightDetailsFragment.Companion.PRICE_BREAKDOWN
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.getParcelableCompat
import net.sharetrip.b2b.util.convertCurrencyToBengaliFormat

class ReissuePricingInformation : BottomSheetDialogFragment() {
    private lateinit var bindingView: ViewPriceDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingView = ViewPriceDetailsBinding.inflate(layoutInflater, container, false)

        val priceInfo: PriceBreakdown =
            requireArguments().getParcelableCompat(PRICE_BREAKDOWN, PriceBreakdown::class.java)!!
        bindingView.apply {
            fareDiffPrice.text = priceInfo.fareDifference.toLong().convertCurrencyToBengaliFormat()
            airlineFeePrice.text = priceInfo.airlinesFee.toLong().convertCurrencyToBengaliFormat()
            stFeePrice.text = priceInfo.stFee.toLong().convertCurrencyToBengaliFormat()
            convenienceFeePrice.text = priceInfo.convenienceFee.toLong().convertCurrencyToBengaliFormat()
            totalAirfarePrice.text = priceInfo.totalAmount.toLong().convertCurrencyToBengaliFormat() + " BDT"

        }
        bindingView.warningIcon.setOnClickListener {
            dismiss()
        }
        return bindingView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

//    override fun layoutId(): Int = R.layout.view_price_details
//
//    override fun getViewModel(): BaseViewModel? = null

//    override fun initOnCreateView() {
//        val priceInfo: PriceBreakdown =
//            requireArguments().getParcelableCompat(PRICE_BREAKDOWN, PriceBreakdown::class.java)!!
//        bindingView.apply {
//            fareDiffPrice.text = priceInfo.fareDifference.toString()
//            airlineFeePrice.text = priceInfo.airlinesFee.toString()
//            stFeePrice.text = priceInfo.stFee.toString()
//            convenienceFeePrice.text = priceInfo.convenienceFee.toString()
//            totalAirfarePrice.text = priceInfo.totalAmount.toString()
//
//        }
//    }
}