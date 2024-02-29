package net.sharetrip.b2b.view.flight.history.vrrvoid.travellerselection

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import net.sharetrip.b2b.databinding.ItemFlightInfoBinding
import net.sharetrip.b2b.util.DateUtil
import net.sharetrip.b2b.view.flight.booking.model.Segments
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateDisplayFromApiWithAMPM
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApiWithDayMonth
import java.text.ParseException
import java.util.Calendar

class FlightSegmentVH(private val binding: ItemFlightInfoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun onBind(segment: Segments) {

        binding.originDestCode.text = segment.type
        binding.airlineName.text = segment.segment?.get(0)?.airlines?.shortName
        binding.departureTime.text = segment.segment?.get(0)?.departureDateTime?.getDateTime()!!.parseDateDisplayFromApiWithAMPM()
        binding.departureDayTime.text = segment.segment[0].departureDateTime?.getDateTime()!!
            .parseDateForDisplayFromApiWithDayMonth() + ", ${
            getWeekDay(
                segment.segment[0].departureDateTime?.getDateTime()
            )
        }"
        binding.arrivalTime.text =
            segment.segment[0].arrivalDateTime?.getDateTime()!!.parseDateDisplayFromApiWithAMPM()
        binding.arrivalDayTime.text = segment.segment[0].arrivalDateTime?.getDateTime()!!
            .parseDateForDisplayFromApiWithDayMonth() + ", ${
            getWeekDay(
                segment.segment[0].arrivalDateTime?.getDateTime()
            )
        }"
        binding.duration.text = segment.segment[0].duration
        binding.flightNumber.text = segment.segment[0].flightNumber
        binding.airlineLogo.load(segment.segment[0].logo)
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

    companion object {
        fun create(parent: ViewGroup) = FlightSegmentVH(
            ItemFlightInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}