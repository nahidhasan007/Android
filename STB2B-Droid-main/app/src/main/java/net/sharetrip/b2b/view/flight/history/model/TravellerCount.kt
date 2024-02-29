package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TravellerCount(
    val adult: Int,
    val child: Int,
    val infant: Int
):Parcelable