package net.sharetrip.b2b.view.flight.history.changeticket

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.view.flight.history.model.VoidDetails
import net.sharetrip.b2b.view.flight.history.model.VoidResponse

class ChangeTicketRepo(private val flightEndPoint: FlightEndPoint) {
    suspend fun modifyTicket(
        voidDetails: VoidDetails,
        actionName: String
    ): GenericResponse<RestResponse<VoidResponse>> {
        return flightEndPoint.modifyTicket(voidDetails,actionName)
    }
}