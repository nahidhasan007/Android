package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

data class GPLoyaltyCheckResponse(
    val loyaltyStatus: String? = "No",
    val success: Boolean? = false,
    val otpExpirationInMin :String
)