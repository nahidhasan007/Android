package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchParamDetail(
    val departureDateTime: String,
    val destination: String,
    val origin: String,
    val sequence: Int
): Parcelable