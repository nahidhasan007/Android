package net.sharetrip.b2b.view.flight.history.model

data class FlightHistoryAction(
    val refund: Boolean,
    val reissue: Boolean,
    val temporaryCancel: Boolean,
    val void: Boolean
)
