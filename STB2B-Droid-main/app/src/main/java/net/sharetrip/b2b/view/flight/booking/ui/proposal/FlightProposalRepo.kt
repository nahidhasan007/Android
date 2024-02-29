package net.sharetrip.b2b.view.flight.booking.ui.proposal

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.network.ServiceGenerator.BASE_URL
import net.sharetrip.b2b.view.flight.booking.model.FlightDetails
import net.sharetrip.b2b.view.flight.booking.model.ProposalDownloadResponse

class FlightProposalRepo(private val endPoint: FlightEndPoint) {

    suspend fun downLoadProposal(flightDetails: FlightDetails): GenericResponse<RestResponse<ProposalDownloadResponse>> =
        endPoint.downloadProposal(
            "${BASE_URL}flight-query/download-flight-proposal", flightDetails
        )
}
