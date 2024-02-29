package net.sharetrip.b2b.view.flight.history.refundDetails

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.view.flight.history.model.ChangeTicketDetails
import net.sharetrip.b2b.view.flight.history.model.RefundRequest

class ChangedFlightRefundDetailsRepo(private val flightEndPoint: FlightEndPoint) {

    suspend fun ticketRequestUpdate(refundRequest: RefundRequest): GenericResponse<RestResponse<ChangeTicketDetails>> =
        flightEndPoint.acceptRefundCharge(refundRequest)
}