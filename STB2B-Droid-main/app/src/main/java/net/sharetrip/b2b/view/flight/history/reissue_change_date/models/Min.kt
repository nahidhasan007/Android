package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Min(
    @Json(name = "direct")
    val direct: Double,
    @Json(name = "nonDirect")
    val nonDirect: Double
) : Parcelable
