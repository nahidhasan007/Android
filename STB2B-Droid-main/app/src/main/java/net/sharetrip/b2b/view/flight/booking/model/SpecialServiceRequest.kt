package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpecialServiceRequest(
    val code: String,
    val name: String
) : Parcelable
