package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import net.sharetrip.b2b.view.flight.booking.model.PriceBreakdown

@Parcelize
data class PriceDetails(
    val couponAmount: Double? = 0.0,
    val currency: String? = "BDT",
    val details: List<PriceBreakdown>? = null,
    val discount: Double? = 0.0,
    val discountAmount: Double? = 0.0,
    var advanceIncomeTax: Double? = 0.0,
    val originPrice: Double? = 0.0,
    val redeemPoints: Double? = 0.0,
    val subTotal: Double? = 0.0,
    val total: Double? = 0.0
) : Parcelable {

    fun getTotalBaseFare(): Double {
        var totalBaseFare = 0.0
        details?.forEach {
            totalBaseFare += it.baseFare * it.numberPaxes
        }
        return totalBaseFare
    }

    fun getTotalTaxes(): Double {
        var totalTax = 0.0
        details?.forEach {
            totalTax += it.tax * it.numberPaxes
        }
        return totalTax
    }

    fun getTotalTraveller(): Int {
        var totalTraveller = 0
        details?.forEach {
            totalTraveller += it.numberPaxes
        }
        return totalTraveller
    }
}