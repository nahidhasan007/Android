package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OriginName(
    val airport: String,
    val city: String,
    val code: String,
    val country: String
) : Parcelable
