package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Points(
    val earning: Int,
    val shareLink: String,
    val shared: Int
) : Parcelable