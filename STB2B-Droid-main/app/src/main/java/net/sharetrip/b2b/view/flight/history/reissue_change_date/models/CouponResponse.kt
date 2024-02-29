package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CouponResponse(
    var discount: String = "",
    var discountType: String = "",
    var withDiscount: String = "No",
    var gateway: List<String> = ArrayList(),
    var mobileVerificationRequired: String? = "No",
    var maximumDiscountAmount: Double = 0.0
) : Parcelable