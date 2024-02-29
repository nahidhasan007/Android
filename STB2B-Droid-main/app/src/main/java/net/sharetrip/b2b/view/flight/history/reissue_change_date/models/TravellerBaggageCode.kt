package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TravellerBaggageCode(
    var luggageCodeList: ArrayList<String> = ArrayList(),
    var totalBaggageCost: Double = 0.0) : Parcelable
