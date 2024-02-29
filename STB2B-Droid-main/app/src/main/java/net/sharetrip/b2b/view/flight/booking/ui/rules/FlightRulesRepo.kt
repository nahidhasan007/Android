package net.sharetrip.b2b.view.flight.booking.ui.rules

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.view.flight.booking.model.AirFareResponse

class FlightRulesRepo(private val endPoint: FlightEndPoint) {

    suspend fun getFlightRules(
        searchId: String, sessionId: String, sequenceCode: String,
    ): GenericResponse<RestResponse<AirFareResponse>> {
        return endPoint.getFlightRules(searchId, sessionId, sequenceCode)
    }
}
