package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomPricing(
    var adult: TravellerItem = TravellerItem(),
    var child: TravellerItem = TravellerItem(),
    var infant: TravellerItem = TravellerItem()
) : Parcelable