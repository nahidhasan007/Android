package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import net.sharetrip.b2b.util.DateUtil.getWeekDay
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateDisplayFromApiWithAMPM
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApiWithDayMonth

@Parcelize
data class Flight(
    val aircraft: String,
    val aircraftCode: String,
    val airline: String,
    val airlinesCode: String,
    val arrival: Arrival,
    val baggageUnit: String,
    val baggageWeight: Int,
    val cabin: String,
    val cabinCode: String,
    val departure: Departure,
    val destination: Destination,
    val destinationCode: String,
    val duration: Int,
    val extraParams: String,
    val fareReference: String,
    val flightNumber: String,
    val flyDuration: String,
    val hiddenStop: String,
    val id: Int,
    val infantBaggageUnit: String,
    val infantBaggageWeight: Int,
    val logo: String,
    val marriageGrp: String,
    val operatedBy: String,
    val origin: Origin,
    val originCode: String,
    val rbdCode: String,
    val seatsBelowMin: String
) : Parcelable {
    @IgnoredOnParcel
    var isFlightSelected: Boolean = false

    val arrivalDate get() = arrival.dateTime.parseDateForDisplayFromApiWithDayMonth() +  ", ${
        getWeekDay(arrival.dateTime)
    }"
    val arrivalTime get() = arrival.dateTime.parseDateDisplayFromApiWithAMPM()

    val departureDate
        get() = departure.dateTime.parseDateForDisplayFromApiWithDayMonth() + ", ${
            getWeekDay(departure.dateTime)
        }"
    val departureTime get() = departure.dateTime.parseDateDisplayFromApiWithAMPM()

}
