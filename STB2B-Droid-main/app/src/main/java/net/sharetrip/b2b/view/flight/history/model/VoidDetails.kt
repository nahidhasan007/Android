package net.sharetrip.b2b.view.flight.history.model

class VoidDetails(
    val requestReason: String?,
    val bookingCode: String,
    val pnrCode: String,
    val passengers: List<Traveller>
)