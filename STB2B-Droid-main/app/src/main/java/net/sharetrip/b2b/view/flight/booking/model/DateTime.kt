package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DateTime(
    val date: String?,
    val time: String?
) : Parcelable {
    fun getDateTime(): String {
        return "$date $time "
    }
}