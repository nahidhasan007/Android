package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceBreakdown(
    val airlinesFee: Int,
    val convenienceFee: Int ,
    val currency: String,
    val fareDifference: Int,
    val stFee: Int,
    val totalAmount: Int
) : Parcelable