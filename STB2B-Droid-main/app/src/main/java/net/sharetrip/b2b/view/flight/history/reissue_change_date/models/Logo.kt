package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import com.squareup.moshi.Json

data class Logo (
    @Json(name = "small")
    var small: String,
    @Json(name = "medium")
    var medium: String,
    @Json(name = "large")
    var large: String
)
