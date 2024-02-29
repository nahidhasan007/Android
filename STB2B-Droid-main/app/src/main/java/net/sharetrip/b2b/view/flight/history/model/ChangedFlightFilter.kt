package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChangedFlightFilter(
    var uuid: String = "",
    var bookingCode: String = "",
    var requestType: String? = null,
    var requestStatus: String? = null,
    var bookingDate: BookingDate? = BookingDate(),
) : Parcelable