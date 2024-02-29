package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DestinationName(
    val airport: String,
    val city: String,
    val code: String,
    val country: String? = null
) : Parcelable