package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AirFareRule(
    val destination: String,
    val destinationCode: String,
    val origin: String,
    val originCode: String,
    val policy: Policy
): Parcelable