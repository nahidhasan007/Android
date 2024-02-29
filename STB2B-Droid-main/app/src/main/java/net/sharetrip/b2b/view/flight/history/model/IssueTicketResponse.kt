package net.sharetrip.b2b.view.flight.history.model

data class IssueTicketResponse(
    val bookingCode: String,
    val bookingId: Int,
    val providerCode: String,
    val type: String
)