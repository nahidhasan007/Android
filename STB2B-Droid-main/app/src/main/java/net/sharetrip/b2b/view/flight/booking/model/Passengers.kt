package net.sharetrip.b2b.view.flight.booking.model

data class Passengers(
    var adult: List<Passenger> = listOf(),
    var child: List<Passenger> = listOf(),
    var infant: List<Passenger> = listOf()
)