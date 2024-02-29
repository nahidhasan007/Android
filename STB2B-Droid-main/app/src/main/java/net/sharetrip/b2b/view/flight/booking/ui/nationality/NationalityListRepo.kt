package net.sharetrip.b2b.view.flight.booking.ui.nationality

import net.sharetrip.b2b.network.FlightEndPoint

class NationalityListRepo(private val endPoint: FlightEndPoint) {

    suspend fun getAllCountryList() = endPoint.getCountryList()
}