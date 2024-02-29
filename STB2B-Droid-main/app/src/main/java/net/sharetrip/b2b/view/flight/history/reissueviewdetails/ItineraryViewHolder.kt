package net.sharetrip.b2b.view.flight.history.reissueviewdetails

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import net.sharetrip.b2b.databinding.ReissueItineraryItemBinding
import net.sharetrip.b2b.util.DateUtil
import net.sharetrip.b2b.view.flight.history.model.ModifyHistory
import net.sharetrip.b2b.view.flight.history.model.ReissueResultDetail
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApiSlashWithHalfYear
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApiWithHalfYear
import java.text.FieldPosition
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ItineraryViewHolder(
    private val binding: ReissueItineraryItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(reissueResultDetail: ReissueResultDetail, position: Int) {
        if(position>0){
            binding.newItinerary.visibility = View.GONE
        }

        binding.locationFrom.text = reissueResultDetail.originName.city // before.originAddress
        binding.airportFrom.text = reissueResultDetail.originName.airport
        binding.airlineName.text = reissueResultDetail.airlines.short
        binding.airlineImg.load(reissueResultDetail.logo)

        binding.locationTo.text =
            reissueResultDetail.destinationName.city // before.destinationAddress
        binding.airportTo.text = reissueResultDetail.destinationName.airport

        binding.date.text = getWeekDay(reissueResultDetail.departureDateTime.date)+", "+reissueResultDetail.departureDateTime.date.parseDateForDisplayFromApiSlashWithHalfYear()
        binding.journeyDayTime.text =
            formatTime(reissueResultDetail.departureDateTime?.time)
        binding.flightNo.text = " ${reissueResultDetail.flightNumber}"

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