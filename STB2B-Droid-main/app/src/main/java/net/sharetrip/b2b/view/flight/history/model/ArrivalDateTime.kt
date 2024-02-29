package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArrivalDateTime(
    val date: String,
    val time: String
) : Parcelable