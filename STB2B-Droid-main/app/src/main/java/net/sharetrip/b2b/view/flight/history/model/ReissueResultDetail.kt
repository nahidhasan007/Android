package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReissueResultDetail(
    val aircraft: String,
    val aircraftCode: String,
    val airlines: Airlines,
    val airlinesCode: String,
    val arrivalDateTime: ArrivalDateTime,
    val baggageUnit: String,
    val baggageWeight: Int,
    var cabin: String? = "",
    val cabinCode: String,
    val departureDateTime: DepartureDateTime,
    val destinationCode: String,
    val destinationName: DestinationName,
    val destinationTerminal: String,
    val duration: String,
    val flightNumber: String,
    val infantBaggageUnit: String,
    val infantBaggageWeight: Int,
    val logo: String,
    val operatedBy: String,
    val originCode: String,
    val originName: OriginName,
    val originTerminal: String
) : Parcelable