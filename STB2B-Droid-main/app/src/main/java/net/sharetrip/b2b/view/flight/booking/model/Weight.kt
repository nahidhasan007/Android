package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weight(
    val key: Int?,
    val weight: Int?,
    val unit: String?,
    val note: String?
): Parcelable