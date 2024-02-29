package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchParams(
    val adult: Int?,
    val airlines: String?,
    val child: Int?,
    @field:Json(name = "class")
    var class_: String? = "",
    val currency: String?,
    val flightType: String?,
    val infant: Int?,
    val nextLink: String?,
    val preferredAirlines: String?,
    val stop: String?,
    val tripType: String?
) : Parcelable