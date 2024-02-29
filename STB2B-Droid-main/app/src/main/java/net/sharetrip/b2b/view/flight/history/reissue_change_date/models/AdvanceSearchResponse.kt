package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdvanceSearchResponse(
    @Json(name = "fare")
    val fare: List<Fare>,
    @Json(name = "max")
    val max: Max,
    @Json(name = "min")
    val min: Min
) : Parcelable
