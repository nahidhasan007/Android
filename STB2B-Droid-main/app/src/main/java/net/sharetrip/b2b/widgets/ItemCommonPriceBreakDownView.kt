package net.sharetrip.b2b.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemCommonPriceBreakDownBinding
import net.sharetrip.b2b.view.flight.history.model.PriceDetails

class ItemCommonPriceBreakDownView : LinearLayout {

    lateinit var bindingView: ItemCommonPriceBreakDownBinding

    constructor(context: Context?) : super(context) {
        initUi()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initUi()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initUi()
    }

    private fun initUi() {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        bindingView =
            DataBindingUtil.inflate(inflater, R.layout.item_common_price_break_down, this, true)
    }

    fun setPriceBreakDown(
        priceDetails: PriceDetails,
        isNavigateFromFlightDetails: Boolean = false
    ) {
        bindingView.priceDetails = priceDetails
        bindingView.isNavigateFromFlightDetails = isNavigateFromFlightDetails

        for (price in priceDetails.details!!) {
            if (price.numberPaxes != 0) {
                bindingView.layoutPriceContainer.addView(ItemPriceView(context).apply {
                    setPriceBreakDown(price)
                })
            }
        }

        bindingView.layoutBaseAndTaxContainer.addView(
            ItemBaseAndTaxFareView(
                context
            ).apply {
                setPriceBreakDown(
                    priceDetails.getTotalTraveller(),
                    priceDetails.getTotalBaseFare(),
                    priceDetails.getTotalTaxes(),
                    priceDetails.advanceIncomeTax!!
                )
            })
    }
}