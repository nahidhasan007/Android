package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Airlines(
    @field:Json(name = "full")
    val fullName: String?,
    @field:Json(name = "short")
    val shortName: String?,
    val code: String?
) : Parcelable
