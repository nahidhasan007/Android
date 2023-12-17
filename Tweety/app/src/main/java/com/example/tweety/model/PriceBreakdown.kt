package com.example.tweety.model

data class PriceBreakdown(
    val airlinesFee: String? = "",
    val fareDifference: String? = "",
    val stFee: String? = "",
    var total: String? = ""
)
