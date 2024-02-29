package net.sharetrip.b2b.view.flight.history.reissue_change_date.utils

import android.text.Layout
import android.text.style.AlignmentSpan
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.PriceBreakdown
import java.text.NumberFormat
import java.util.Locale

object PriceBreakDownUtil {
    @JvmStatic
    fun getFormattedPriceBreakDownForReissue(mPriceBreakdown: PriceBreakdown?): CharSequence {
        val mPriceTruss = Truss()
        val details = mPriceBreakdown?.details
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        if (details != null) {
            for (mDetail in details) {
                if (mDetail.numberPaxes == 0) continue
                val type = mDetail.type.toLowerCase()
                mPriceTruss.append(type.substring(0, 1).toUpperCase() + type.substring(1) + " * " + mDetail.numberPaxes)
                mPriceTruss.append(Strings.LINE_BREAK)
                mPriceTruss.append("Base Fare * " + mDetail.numberPaxes)
                mPriceTruss.pushSpan(LineOverlapSpan())
                mPriceTruss.append(Strings.LINE_BREAK)
                mPriceTruss.popSpan()
                mPriceTruss.pushSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE))
                val basefare = mDetail.baseFare.toDouble() * mDetail.numberPaxes.toDouble()
                mPriceTruss.append(numberFormat.format(basefare.toLong()))
                mPriceTruss.popSpan()
                mPriceTruss.append(Strings.LINE_BREAK)
                mPriceTruss.append("Taxes & Fees * " + mDetail.numberPaxes)
                mPriceTruss.pushSpan(LineOverlapSpan())
                mPriceTruss.append(Strings.LINE_BREAK)
                mPriceTruss.popSpan()
                mPriceTruss.pushSpan(AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE))
                val tax = mDetail.tax.toDouble() * mDetail.numberPaxes.toDouble()
                mPriceTruss.append(numberFormat.format(tax.toLong()))
                mPriceTruss.popSpan()
                mPriceTruss.append(Strings.LINE_BREAK)
                mPriceTruss.append(Strings.LINE_BREAK)
            }
        }
        return mPriceTruss.build()
    }

}