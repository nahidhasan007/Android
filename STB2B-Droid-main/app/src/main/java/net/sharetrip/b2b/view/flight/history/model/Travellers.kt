package net.sharetrip.b2b.view.flight.history.model

data class Travellers(
    var adult: List<Traveller> = listOf(),
    var child: List<Traveller> = listOf(),
    var infant: List<Traveller> = listOf()
)