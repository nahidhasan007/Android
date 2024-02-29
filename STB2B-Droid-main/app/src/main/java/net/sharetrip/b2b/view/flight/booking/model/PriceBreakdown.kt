package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class PriceBreakdown(
    val type: String,
    var baseFare: Double,
    var tax: Double,
    var total: Double,
    val currency: String,
    val numberPaxes: Int
) : Parcelable