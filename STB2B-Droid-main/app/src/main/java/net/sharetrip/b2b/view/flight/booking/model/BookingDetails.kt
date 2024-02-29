package net.sharetrip.b2b.view.flight.booking.model

data class BookingDetails(
    val searchId: String,
    val sequenceCode: String,
    val sessionId: String,
    val passengers: Passengers,
    val contactInfo: ContactInfo,
    val reference: String,
    val specialNote: String
)
