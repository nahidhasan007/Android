package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactInfo(
    var mobile: String = "",
    var email: String = ""
) : Parcelable {
    fun isValid(): Boolean =
        mobile.isNotEmpty() && email.isNotEmpty()
}
