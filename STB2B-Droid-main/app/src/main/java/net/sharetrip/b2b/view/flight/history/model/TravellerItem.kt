package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class TravellerItem(
    val baseFare: Double = 0.0,
    val taxFare: Double = 0.0
) : Parcelable