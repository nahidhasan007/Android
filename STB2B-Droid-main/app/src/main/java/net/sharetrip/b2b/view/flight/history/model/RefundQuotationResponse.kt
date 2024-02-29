package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RefundQuotationResponse(
    val automationType: Boolean,
    val confirmationTime: Int? = null,
    val expiresAt: String,
    val msg: String,
    val refundSearchId: String,
    val status: String,
    val success: Boolean,
    val airlineRefundCharge: Long? = 0,
    val stFee: Long? = 0,
    val totalFee: Long? = 0,
    val partialAdjustmentAmount: Long? = 0,
    val purchasePrice: Long? = 0,
    val totalRefundAmount: Long? = 0,
) : Parcelable

/*
 {
    "code":"SUCCESS",
    "message":"Success",

    "response":{
        x"success":true,
        x"refundSearchId":"b9372b70-8c10-11ee-8bb6-e7a100e6b557",
        x"status":"QUOTED",
        x"automationType":true,
        x"msg":"Please Check The Following Quotation",
        x"confirmationTime":300,
        x"expiresAt":"2023-11-26 10:23:16",
        "airlineRefundCharge":1500,
        "stFee":100,
        "totalFee":1600,
        "partialAdjustmentAmount":0,
        "purchasePrice":4885,
        "totalRefundAmount":3285
    }}
* */

/*{
    "success":true,
    "refundSearchId":"ee8559a0-8454-11ee-a337-47277e7832a5",
    "status":"REQUESTED",
    "automationType":false,
    "msg":"Your manual refund request has been received. Our flight team is preparing a refund quotation for you. We will notify you via email once your quotation is ready.",
    "confirmationTime":null,
    "expiresAt":"2023-11-18 13:51:21"


    {
        "success": true,
        "refundSearchId": "a1e8ded0-8474-11ee-a337-47277e7832a5",
        "status": "REQUESTED",
        "automationType": false,
        "msg": "Your manual refund request has been received. Our flight team is preparing a refund quotation for you. We will notify you via email once your quotation is ready.",
        "confirmationTime": null,
        "expiresAt": "2023-11-18 17:38:16"
    }
}*/

/*
vrr response

void -> flight Details bottomsheet dialog fragment
* */
