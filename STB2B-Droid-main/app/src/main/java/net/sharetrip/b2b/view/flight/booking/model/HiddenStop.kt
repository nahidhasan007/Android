package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HiddenStop(
    val city: String?,
    val airport: String?,
    val code: String?,
    val country: String?
) : Parcelable