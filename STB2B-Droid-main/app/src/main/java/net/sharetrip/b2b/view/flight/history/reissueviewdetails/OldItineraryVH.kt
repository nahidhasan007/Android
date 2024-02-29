package net.sharetrip.b2b.view.flight.history.reissueviewdetails

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.OldItineraryItemBinding
import net.sharetrip.b2b.databinding.ReissueItineraryItemBinding
import net.sharetrip.b2b.util.DateUtil
import net.sharetrip.b2b.view.flight.history.model.OldResultDetail
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApiSlashWithHalfYear
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApiWithHalfYear
import java.text.FieldPosition
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class OldItineraryVH(
    private val binding: OldItineraryItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ResourceAsColor")
    fun onBind(oldResultDetail: OldResultDetail, position: Int) {
        if(position>0){
            binding.newItinerary.visibility = View.GONE
        }
        binding.newItinerary.text = "Old Itinerary"

        binding.locationFrom.text = oldResultDetail.originName.city // before.originAddress
        binding.airportFrom.text = oldResultDetail.originName.airport
        binding.airlineName.text = oldResultDetail.airlines.short
        binding.airlineImg.load(oldResultDetail.logo)

        binding.locationTo.text =
            oldResultDetail.destinationName.city
        binding.airportTo.text = oldResultDetail.destinationName.airport

        binding.date.text = getWeekDay(oldResultDetail.departureDateTime.date)+", "+oldResultDetail.departureDateTime.date.parseDateForDisplayFromApiSlashWithHalfYear()
        binding.journeyDayTime.text =
            formatTime(oldResultDetail.departureDateTime.time)
        binding.flightNo.text = " ${oldResultDetail.flightNumber}"

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