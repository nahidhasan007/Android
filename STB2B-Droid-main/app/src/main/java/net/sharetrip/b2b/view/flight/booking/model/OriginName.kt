package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OriginName (
    val city: String?,
    val airport: String?,
    val code: String?
): Parcelable