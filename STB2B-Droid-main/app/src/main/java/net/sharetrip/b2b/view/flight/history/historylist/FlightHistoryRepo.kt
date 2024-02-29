package net.sharetrip.b2b.view.flight.history.historylist

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.view.flight.history.model.FlightHistoryResponse

class FlightHistoryRepo(private val flightEndPoint: FlightEndPoint) {
    suspend fun getFlightHistoryList(
        offset: Int,
        status: String?
    ): GenericResponse<RestResponse<FlightHistoryResponse>> {
        return flightEndPoint.getFlightHistory( offset=offset, status = status)
    }
}