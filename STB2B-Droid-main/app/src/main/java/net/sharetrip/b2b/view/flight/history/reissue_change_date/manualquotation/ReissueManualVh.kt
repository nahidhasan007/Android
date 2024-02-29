package net.sharetrip.b2b.view.flight.history.reissue_change_date.manualquotation

import androidx.recyclerview.widget.RecyclerView
import coil.load
import net.sharetrip.b2b.databinding.OldNewItineraryItemBinding
import net.sharetrip.b2b.util.DateUtil
import net.sharetrip.b2b.util.dateChangeToHourDDMMYYYYFromT
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlight
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.Segment
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.dateChangeToHourWeekdayFromT
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApi
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApiWithAMPM
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApiWithHalfYear
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReissueManualVh(
    private val binding: OldNewItineraryItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(mPair: Pair<ReissueFlight, Segment>, totalChanges: Int, flightNoBefore : String) {
        val before: ReissueFlight = mPair.first
        val after: Segment = mPair.second

        for (transit in after.transits) {
            if (transit.flightNumber != null) {
                binding.flightNo2.text = "${transit.airlinesCode}${transit.flightNumber}"
                break
            }
        }


        binding.totalChanges.text = "$totalChanges Changes"

        binding.locationFrom.text = before.origin?.city // before.originAddress
        binding.airportFrom.text = before.origin?.code+ ",${before.origin?.airport}"
        binding.airlineName.text = after.airlines.shortName
        binding.airlineImg.load(after.logo)

        binding.locationTo.text = before.destination?.city // before.destinationAddress
        binding.airportTo.text = before.destination?.code + ",${before.destination?.airport}"

        binding.date.text = getWeekDay(before.departure?.dateTime)+", "+before.departure?.dateTime?.parseDateForDisplayFromApiWithHalfYear()
        binding.journeyDayTime.text = before.departure?.dateTime?.parseDateForDisplayFromApiWithAMPM()
        binding.flightNo.text = flightNoBefore

        binding.locationFrom2.text = after.originName.city  // before.originAddress
        binding.airportFrom2.text = after.originCode+",${after.originName.airport}"
        binding.airlineName2.text = after.airlines.shortName
        binding.airlineImg2.load(after.logo)

        binding.locationTo2.text = after.destinationName.city  // before.destinationAddress
        binding.airportTo2.text = after.destinationCode+",${after.destinationName.airport}"

        binding.date2.text = getWeekDay(after.departureDateTime.date)+", "+after.departureDateTime.date?.parseDateForDisplayFromApiWithHalfYear()
        binding.journeyDayTime2.text = formatTime(after.departureDateTime.time)
    }

    private fun formatTime(inputTime: String): String {
        try {
            val inputFormat = SimpleDateFormat("HH:mm", Locale.US)
            val outputFormat = SimpleDateFormat("hh:mm a", Locale.US)
            val date = inputFormat.parse(inputTime)
            return outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getWeekDay(dateString: String?): String {
        val calendar = Calendar.getInstance()
        try {
            calendar.timeInMillis = DateUtil.parseDateToMillisecond(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val index = calendar[Calendar.DAY_OF_WEEK]
        return DateUtil.days[index - 1]
    }


}