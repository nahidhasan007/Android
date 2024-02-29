package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DepartureDateTime(
    val date: String,
    val time: String
) : Parcelable
