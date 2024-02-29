package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VRRQuotationResponse(
    val airlineVoidCharge: Int? = null,
    val confirmationTime: Int? = null,
    val expiresAt: String? = null,
    val msg: String,
    val partialAdjustmentAmount: Int? = null,
    val purchasePrice: Int,
    val reQuotationTime: String,
    val stVoidCharge: Int? = null,
    val status: String,
    val success: Boolean,
    val totalFee: Int? = null,
    val totalReturnAmount: Int? = null,
    val voidSearchId: String
) : Parcelable
