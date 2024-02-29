package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Baggage(
    val type: String? = null,
    var adult: String? = null,
    var child: String? = null,
    var infant: String? = null
) : Parcelable
