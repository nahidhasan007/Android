package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChildrenDOB(
    var title: String = "",
    var date: String = ""
) : Parcelable