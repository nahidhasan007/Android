package net.sharetrip.b2b.view.flight.history.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReissueSearch(
    val msg: String,
    val reissueSearchId: String,
    val status: String
) : Parcelable
