package net.sharetrip.b2b.view.flight.booking.ui.airport

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.view.flight.booking.model.Airport

class AirportSearchRepo(private val endPoint: FlightEndPoint) {
    suspend fun fetchAirportListByKeyWord(keyWord: String): GenericResponse<RestResponse<List<Airport>>> =
        endPoint.fetchAirportList(keyWord)
}
