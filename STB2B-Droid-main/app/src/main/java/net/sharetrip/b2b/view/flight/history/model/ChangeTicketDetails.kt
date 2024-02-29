package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChangeTicketDetails(
    val createdAt: String?,
    val updatedAt: String?,
    val uuid: String?,
    val expiredAt: String?,
    val requestReason: String?,
    val requestType: String?,
    val requestStatus: String?,
    val note: String? = null,
    val refundCharge: Double = 0.0,
    val passengers: List<Traveller>,
    val bookingHistory: BookingHistory? = null,
    val refundDetail: RefundDetail?,
    val refundProcessingTime: Double?,
    val amountToBeRefunded: Double?,
    val isDomestic: Boolean,
    val actualBookingHistory: ActualBookingHistory?
): Parcelable