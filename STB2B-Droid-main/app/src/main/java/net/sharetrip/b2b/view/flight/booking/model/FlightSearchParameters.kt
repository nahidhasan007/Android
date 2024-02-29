package net.sharetrip.b2b.view.flight.booking.model

data class FlightSearchParameters(
    val tripType: String = "",
    val adultCount: Int = 0,
    val childCount: Int = 0,
    val infantCount: Int = 0,
    val flightClass: String = "",
    val origin: String = "",
    val destination: String = "",
    val departureTime: String = "",
    val arrivalTime: String = ""
)