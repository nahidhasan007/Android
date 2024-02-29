package net.sharetrip.b2b.view.flight.booking.ui.maildetails

import net.sharetrip.b2b.model.EmptyResponse
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.network.ServiceGenerator.BASE_URL
import net.sharetrip.b2b.view.flight.booking.model.FlightDetails

class MailDetailsRepo(private val endPoint: FlightEndPoint) {
    suspend fun senDMail(flightDetails: FlightDetails): GenericResponse<RestResponse<EmptyResponse>> =
        endPoint.sendMail(
            "${BASE_URL}flight-query/send-flight-proposal-mail-for-app",
            flightDetails
        )
}