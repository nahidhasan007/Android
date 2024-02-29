package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterParams(
        var price: Price? = null,
        var isRefundable: List<Int>? = null,
        var outbound: String? = null,
        var airlines: List<String>? = null,
        var weight: List<Int>? = null,
        var stops: List<Int>? = null,
        var layover: List<String>? = null,
        var returnTime: String? = null,
        var sort: String? = null
) : Parcelable