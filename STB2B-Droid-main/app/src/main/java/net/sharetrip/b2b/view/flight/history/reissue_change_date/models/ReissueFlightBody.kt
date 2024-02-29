package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

data class ReissueFlightBody(
    val uuid :String,
    val date: String,
    val destination: String,
    val origin: String,
    val flightStatusOptions : List<String>? = null
)