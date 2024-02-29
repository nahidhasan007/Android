package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RefundDetail(
    val amountToBeRefunded: Double? = 0.0,
    val farePaidByAgentUsed: Double? = 0.0,
    val refundApplicableAmount: Double? = 0.0,
    val refundCharge: Double? = 0.0,
    val refundServiceCharge: Double? = 0.0,
    val totalRefundCharge: Double? = 0.0,
    var uuid: String?
) : Parcelable