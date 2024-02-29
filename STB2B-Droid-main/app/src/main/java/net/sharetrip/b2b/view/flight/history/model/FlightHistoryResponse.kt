package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlightHistoryResponse(
    val data: List<FlightHistory>,
    val limit: Int,
    val offset: Int,
    val count: Int
) : Parcelable