package net.sharetrip.b2b.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemBaseFareNTaxBinding

class ItemBaseAndTaxFareView : LinearLayout {

    lateinit var bindingView: ItemBaseFareNTaxBinding

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
        bindingView = DataBindingUtil.inflate(inflater, R.layout.item_base_fare_n_tax, this, true)
    }

    fun setPriceBreakDown(
        totalTraveller: Int,
        totalFare: Double,
        totalTax: Double,
        totalAIT: Double
    ) {
        bindingView.totalTraveller = totalTraveller
        bindingView.totalBaseFare = totalFare
        bindingView.totalTax = totalTax
        bindingView.totalAIT = totalAIT
    }
}