package net.sharetrip.b2b.view.flight.booking.model

import com.squareup.moshi.Json

data class FlightSearchResponse (
    val advice: String?,
    val items: Int?,
    val totalRecords: Int?,
    @field:Json(name = "class")
    val class_: String?,
    val tripType: String?,
    val searchId: String?,
    val sessionId: String?,
    val filters: FlightFilter?,
    val flights: List<Flights>?
)