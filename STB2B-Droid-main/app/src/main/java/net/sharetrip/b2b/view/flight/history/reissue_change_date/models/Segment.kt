package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import net.sharetrip.b2b.view.flight.booking.model.Airlines

@Parcelize
data class Segment(
    val airlines: Airlines,
    val airlinesCode: String,
    val arrivalDateTime: ArrivalDateTime,
    val arrivalDateTimeIso: String,
    val departureDateTime: DepartureDateTime,
    val departureDateTimeIso: String,
    val destinationCode: String,
    val destinationName: DestinationName,
    val duration: String,
    val durationInMinutes: Int,
    val hiddenStops: Boolean,
    val logo: String,
    val originCode: String,
    val originName: OriginName,
    val reissueSearchCode: String,
    val reissueSequenceCode: String,
    val transits: List<Transit>
) : Parcelable