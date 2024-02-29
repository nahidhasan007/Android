package net.sharetrip.b2b.view.flight.history.changelist

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.view.flight.history.model.ChangeTicketListResponse

class ChangedFlightHistoryListRepo(private val flightEndPoint: FlightEndPoint) {

    suspend fun exchangeRequest(
        offset: Int,
        filter: String? = null
    ): GenericResponse<RestResponse<ChangeTicketListResponse>> =
        flightEndPoint.exchangeRequest(offset, filter = filter)

}