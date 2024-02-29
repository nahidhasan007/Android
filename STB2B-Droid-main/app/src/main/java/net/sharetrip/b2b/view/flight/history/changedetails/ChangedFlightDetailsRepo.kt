package net.sharetrip.b2b.view.flight.history.changedetails

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.view.flight.history.model.ChangeTicketDetails
import net.sharetrip.b2b.view.flight.history.model.FlightHistory

class ChangedFlightDetailsRepo(private val flightEndPoint: FlightEndPoint, val uuid: String) {

    suspend fun exchangeRequestDetails(): GenericResponse<RestResponse<ChangeTicketDetails>> =
        flightEndPoint.exchangeRequestDetails(uuid)

    suspend fun getActualBookingDetails(
        bookingCode: String,
        pnrCode: String
    ): GenericResponse<RestResponse<FlightHistory>> =
        flightEndPoint.getActualBookingDetails(bookingCode, pnrCode)

}