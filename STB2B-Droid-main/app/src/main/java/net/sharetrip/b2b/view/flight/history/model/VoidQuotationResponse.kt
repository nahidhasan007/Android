package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VoidQuotationResponse(
    val airlineVoidCharge: Int,
    val automationType: Boolean,
    val confirmationTime: Int?,
    val msg: String,
    val purchasePrice: Int,
    val reQuotationTime: String,
    val stVoidCharge: Int,
    val status: String,
    val totalFee: Int,
    val totalReturnAmount: Int,
    val voidSearchId: String,
    val expiresAt : String? = null
): Parcelable

/*{
    "success":true,
    "automationType":1,
    "status":"QUOTED",
    "msg":"Please check the following quotation.",
    "reQuotationTime":"23:55:00",
    "confirmationTime":300,
    "expiresAt":"2023-11-19 11:22:49",
    "voidSearchId":"2c8830b0-86cb-11ee-a337-47277e7832a5",
    "airlineVoidCharge":0,
    "stVoidCharge":100,
    "partialAdjustmentAmount":0,
    "totalFee":100,
    "purchasePrice":9158,
    "totalReturnAmount":9058
}*/