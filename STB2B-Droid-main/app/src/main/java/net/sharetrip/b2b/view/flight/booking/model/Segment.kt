package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import net.sharetrip.b2b.R
import net.sharetrip.b2b.util.MsgUtils

@Parcelize
data class Segment(
    val searchCode: String?,
    val sequenceCode: String?,
    val airlines: Airlines?,
    val airlinesCode: String?,
    val flightNumber: String?,
    val logo: String?,
    val aircraft: String?,
    val aircraftCode: String?,
    val originCode: String?,
    val originName: OriginName?,
    val destinationCode: String?,
    val destinationName: OriginName?,
    val arrivalDateTime: DateTime?,
    val departureDateTime: DateTime?,
    val duration: String?,
    val baggageWeight: Int?,
    val baggageUnit: String?,
    val seatsRemaining: Int?,
    val cabin: String?,
    val hiddenStop: HiddenStop?,
    val dayCount: Int?,
    var transitTime: String?,
    val cabinClass: CabinClass?,
    var classType: String
) : Parcelable {

    fun transitText(): String {
        if (destinationCode == "HKG" && isNextCalenderDay())
            return MsgUtils.transitMoreThanTwelveHours

        if (transitTime == null || transitTime!!.isEmpty()) return ""

        val times = transitTime!!.split(" ")
        val hours = times[0].replace("h", "").toInt()

        if (hours >= 24)
            return MsgUtils.transitMoreThan24Hours

        if (hours >= 12 && destinationCode == "BKK" || destinationCode == "DMK")
            return MsgUtils.transitMoreThanTwelveHours

        return ""
    }

    private fun isNextCalenderDay(): Boolean {
        val arrivalTime = arrivalDateTime?.time?.split(":")
        val hour = arrivalTime?.get(0)
        val time = arrivalTime?.get(1)

        if (transitTime != null) {
            val transit = transitTime?.trim()?.split(" ")
            val addedHour =
                if (transit?.size?.let { it > 0 } == true) transit[0].replace("h", "") else "0"
            val addedTime =
                if (transit?.size?.let { it > 1 } == true) transit[1].replace("m", "") else "0"


            var totalHour = 0

            if (hour != null && time != null && addedHour.isNotEmpty() && addedTime.isNotEmpty()) {
                if ((time.toInt() + addedTime.toInt()) >= 60)
                    totalHour += 1

                totalHour += hour.toInt().plus(addedHour.toInt())
            }
            if (totalHour >= 24) return true

            return false

        } else return false
    }
}
