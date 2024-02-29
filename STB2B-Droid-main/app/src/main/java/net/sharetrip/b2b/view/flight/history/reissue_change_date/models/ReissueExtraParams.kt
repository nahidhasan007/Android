package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

data class ReissueExtraParams(
    val totalChanges: Int?= 0,
    val flightNumberAfter: String? = null,
    val FlightNoBefore: String? = null
)
