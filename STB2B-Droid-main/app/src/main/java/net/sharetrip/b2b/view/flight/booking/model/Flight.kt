package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.IgnoredOnParcel

@Parcelize
data class Flight(
    val searchCode: String?,
    val sequenceCode: String?,
    val airlines: Airlines?,
    val logo: String?,
    val aircraft: String?,
    val aircraftCode: String?,
    val airlinesCode: String?,
    val originName: OriginName?,
    val destinationName: OriginName?,
    val arrivalDateTime: DateTime?,
    val departureDateTime: DateTime?,
    val duration: String?,
    val stop: Int?,
    val hiddenStops: Boolean,
    val stopSegment: List<Airport>?,
    val min: Int?,
    val dayCount: Int?,
    val hiddenStop: Boolean?
) : Parcelable {
    @IgnoredOnParcel
    var isFlightSelected : Boolean = false
}
