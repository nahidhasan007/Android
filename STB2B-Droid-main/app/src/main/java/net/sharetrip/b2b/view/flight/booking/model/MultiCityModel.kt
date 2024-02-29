package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MultiCityModel(
        var origin: String = "",
        var destination: String = "",
        var departDate: String = ""
) : Parcelable