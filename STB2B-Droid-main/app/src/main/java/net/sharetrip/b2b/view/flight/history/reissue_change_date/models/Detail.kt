package net.sharetrip.b2b.view.flight.history.reissue_change_date.models
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Detail(
    var baseFare: Double,
    val currency: String,
    val numberPaxes: Int,
    var tax: Double,
    val total: Double,
    val type: String
) : Parcelable
