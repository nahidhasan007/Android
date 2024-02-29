package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rule(
    val code: Int,
    val text: String,
    val type: String
): Parcelable