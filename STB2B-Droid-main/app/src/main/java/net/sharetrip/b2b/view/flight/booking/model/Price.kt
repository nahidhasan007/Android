package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Price(
    val max: Float?,
    val min: Float?
): Parcelable