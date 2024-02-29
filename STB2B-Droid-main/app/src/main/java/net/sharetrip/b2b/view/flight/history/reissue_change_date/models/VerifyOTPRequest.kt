package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

data class VerifyOTPRequest(
    val mobileNumber: String,
    val otp: String? = null
)