package net.sharetrip.b2b.view.topup.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopUp(
    var amount: Double = 0.0,
    var gateway: String = ""
) : Parcelable