package net.sharetrip.b2b.view.flight.booking.model

import net.sharetrip.b2b.util.MsgUtils

data class FlightDetails(
    val accessToken: String = MsgUtils.accessToken,
    var bcc: String = "",
    var bodyPrefix: String = "",
    var cancellationPolicy: String = "false",
    var cc: String = "",
    var flightClass: String = "Economy",
    var flightData: List<Flights> = ArrayList(),
    var recipient: String = "",
    var searchId: String = "",
    var subject: String = "",
    var tripType: String = "Other",
) {
    fun setMailDetails(
        mailDetails: MailDetails, flightSearch: FlightSearch,
        updatedFlightList: List<Flights>, cancelPolicy: String,
    ) {
        cc = mailDetails.cc
        bcc = mailDetails.bcc
        recipient = mailDetails.recipent
        subject = mailDetails.subject
        bodyPrefix = mailDetails.messageBody
        searchId = flightSearch.searchId
        tripType = flightSearch.tripType
        flightClass = flightSearch.travellersInfo.classType
        cancellationPolicy = cancelPolicy
        flightData = updatedFlightList
    }
}
