package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Segments(
    val type: String?,
    val segment: List<Segment>?
): Parcelable