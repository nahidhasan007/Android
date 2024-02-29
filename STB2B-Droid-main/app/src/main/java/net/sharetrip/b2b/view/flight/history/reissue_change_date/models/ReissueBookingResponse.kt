package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

data class ReissueBookingResponse(
    val automationSupported: Boolean?,
    val payment: Payment?,
    val paymentRequired: Boolean?,
    val reissueCode: String?,
    val reissueConfirmed: Boolean?
)
