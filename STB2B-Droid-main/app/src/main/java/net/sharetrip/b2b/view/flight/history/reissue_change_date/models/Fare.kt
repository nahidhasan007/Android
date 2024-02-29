package net.sharetrip.b2b.view.flight.history.reissue_change_date.models
import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fare(
    @Json(name = "date")
    val date: String,
    @Json(name = "direct")
    val direct: Int,
    @Json(name = "nonDirect")
    val nonDirect: Int
) : Parcelable
