package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Airline (
    val records: Int?,
    val code: String?,
    val full: String?,
    val short: String?
): Parcelable