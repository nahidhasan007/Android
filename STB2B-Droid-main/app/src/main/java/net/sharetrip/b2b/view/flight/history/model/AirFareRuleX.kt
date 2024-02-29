package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AirFareRuleX(
    val destination: String,
    val destinationCode: String,
    val origin: String,
    val originCode: String,
    val policy: String
) : Parcelable