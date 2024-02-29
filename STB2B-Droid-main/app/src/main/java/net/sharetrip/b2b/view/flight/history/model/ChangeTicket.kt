package net.sharetrip.b2b.view.flight.history.model

data class ChangeTicket(
    val requestType: String? = null,
    val bookingHistory: BookingHistory? = null,
    val uuid: String? = null,
    val requestStatus: String? = null
)