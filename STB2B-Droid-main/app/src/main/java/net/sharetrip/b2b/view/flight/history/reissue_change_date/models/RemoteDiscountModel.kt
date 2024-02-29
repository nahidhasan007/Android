package net.sharetrip.b2b.view.flight.history.reissue_change_date.models
import com.squareup.moshi.Json

data class RemoteDiscountModel(
    @Json(name = "subtitle")
    val subtitle: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "type")
    val type: String
)
