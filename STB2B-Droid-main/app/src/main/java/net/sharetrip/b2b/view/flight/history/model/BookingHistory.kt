package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookingHistory(
	val pnrCode: String? = null,
	val bookingCode: String? = null
): Parcelable