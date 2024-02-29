package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookingDetails(
    val airFareRules: List<AirFareRuleX>,
    val airlineResCode: String,
    val airlines: String,
    val bookingCode: String,
    val bookingStatus: String,
    val flyDate: String,
    val numberOfTravellers: Int,
    val paymentStatus: String,
    val pnrCode: String,
    val reissueCode: String,
    val route: String,
    val totalAmount: Int
) : Parcelable