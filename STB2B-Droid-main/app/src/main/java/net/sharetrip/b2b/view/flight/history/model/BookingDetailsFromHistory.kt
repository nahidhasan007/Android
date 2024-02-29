package net.sharetrip.b2b.view.flight.history.model
import net.sharetrip.b2b.view.flight.booking.model.ContactInfo

data class BookingDetailsFromHistory(
    val searchId: String,
    val sequenceCode: String,
    val sessionId: String,
    val passengers: Travellers,
    val contactInfo: ContactInfo,
    val reference: String,
    val specialNote: String
)