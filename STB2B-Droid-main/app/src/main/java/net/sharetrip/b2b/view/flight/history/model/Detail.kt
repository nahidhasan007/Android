package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Detail(
    val baseFare: Int,
    val currency: String,
    val numberPaxes: Int,
    val tax: Int,
    val total: Int,
    val type: String
): Parcelable
