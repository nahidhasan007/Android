package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

data class DestinationPath(
    val origin: String? ="",
    val originCity: String? = "",
    val originAddress: String?= "",
    val destination: String? = "",
    val destinationCity: String? ="",
    val destinationAddress: String?="",
    val dayOfDate: String?="",
    val monthOfDate: String?="",
    val yearOfDate: String?="",
    val originAirport: String? = "",
    val destinationAirport: String? = "",
)
