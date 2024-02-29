package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PriceBreakdown(
    val advanceIncomeTax: Double? = 0.0,
    val airlinesFee: Double? = 0.0,
    var couponAmount: Double? = 0.0,
    var currency: String? = null,
    val details: List<Detail>? = null,
    val discount: Double? = 0.0,
    var discountAmount: Double? = 0.0,
    val fareDifference: Double? = 0.0,
    var originPrice: Double? = 0.0,
    var promotionalAmount: Double? = 0.0,
    var promotionalDiscount: Double = 0.0,
    val stFee: Double? = 0.0,
    val stFeeType: Int? = 0,
    var total: Double? = 0.0
) : Parcelable