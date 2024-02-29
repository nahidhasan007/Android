package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import net.sharetrip.b2b.view.flight.booking.model.Airlines
import net.sharetrip.b2b.view.flight.booking.model.HiddenStop

@Parcelize
data class Transit(
    val aircraft: String,
    val aircraftCode: String,
    val airlines: Airlines,
    val airlinesCode: String,
    val arrivalDateTime: ArrivalDateTime,
    val baggageUnit: String,
    val baggageWeight: Int,
    val cabin: String,
    val cabinCode: String,
    val departureDateTime: DepartureDateTime,
    val destinationCode: String,
    val destinationName: DestinationName,
    val destinationTerminal: String,
    val duration: String,
    val durationInMinutes: Int,
    val flightNumber: String,
    val hiddenStop: HiddenStop,
    val infantBaggageUnit: String,
    val infantBaggageWeight: Int,
    //TODO:: temporarily avoided : val layover: Any?,
    val layoverInMinutes: Int,
    val logo: String,
    val marketingAirline: String,
    val marriageGrp: String,
    val mealCode: String,
    val operatedBy: String,
    val originCode: String,
    val originName: OriginName,
    val originTerminal: String,
    val reissueSearchCode: String,
    val reissueSequenceCode: String,
    val resBookDesigCode: String
) : Parcelable
