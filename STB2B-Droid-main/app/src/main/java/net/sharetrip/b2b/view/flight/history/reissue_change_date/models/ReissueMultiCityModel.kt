package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReissueMultiCityModel(
    var origin: String = "",
    var originCity: String? = "",
    var originAddress: String? = "",
    var originAirport: String? = "",
    var destinationAirport: String? = "",
    var destination: String = "",
    var destinationCity: String? = "",
    var destinationAddress: String? = "",
    var departDate: String = ""
) : Parcelable
