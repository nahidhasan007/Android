package net.sharetrip.b2b.view.flight.history.reissue_change_date.multicity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemMultiCityReissueBinding
import net.sharetrip.b2b.view.flight.booking.model.MultiCityModel
import net.sharetrip.b2b.view.flight.history.model.FlightHistory
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.DestinationPath
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueEligibilityResponse
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueMultiCityModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.getDay
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.getMonth
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.getYear
import net.sharetrip.b2b.util.DateUtil
import net.sharetrip.b2b.util.DateUtil.API_DATE_PATTERN
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MultiCityAdapter(
    private var dataList: MutableList<ReissueMultiCityModel>,
    private val reissueEligibilityResponse: ReissueEligibilityResponse,
    private val formListener: (Int) -> Unit,
    private val toListener: (Int) -> Unit,
    private val dateListener: (Int, String, String) -> Unit
) : RecyclerView.Adapter<MultiCityAdapter.MultiCityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiCityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cityBinding = DataBindingUtil.inflate<ItemMultiCityReissueBinding>(
            inflater,
            R.layout.item_multi_city_reissue, parent, false
        )
        return MultiCityViewHolder(cityBinding)
    }

    override fun onBindViewHolder(holder: MultiCityViewHolder, position: Int) {
        val item = dataList[position]
        val path = DestinationPath(
            item.origin,
            item.originCity,
            item.originAddress,
            item.destination,
            item.destinationCity,
            item.destinationAddress,
            getdayfromDate(item.departDate).toString(),
            getMonth(getmonthfromDate(item.departDate)),
            getWeekDay(item.departDate) +
                    ", "+getYearfromDate(item.departDate).toString(),
            item.originAirport,
            item.destinationAirport
        )
        holder.multiCityBinding.data = path
//
//        holder.multiCityBinding.fromLayout.setOnClickListener {
//        }

//        if (reissueEligibilityResponse.isJourneyChange) {
//            holder.multiCityBinding.toLayout.setOnClickListener {
//                toListener.invoke(position)
//            }
//            holder.multiCityBinding.fromLayout.setOnClickListener {
//                formListener.invoke(position)
//            }
//        }


        holder.multiCityBinding.departureDateLayout.setOnClickListener {
            dateListener(
                position,
                holder.multiCityBinding.fromCodeTextView.text.toString(),
                holder.multiCityBinding.toCodeTextView.text.toString()
            )
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun addItem(cityModel: ReissueMultiCityModel) {
        dataList.add(cityModel)
        notifyDataSetChanged()
    }

    fun removeItem() {
        dataList.removeLast()
        notifyDataSetChanged()
    }

    fun resetItems(newList: List<ReissueMultiCityModel>) {
        dataList = newList as MutableList<ReissueMultiCityModel>
    }

    fun changeItemAtPos(pos: Int, item: ReissueMultiCityModel) {
        if (pos in 0 until dataList.size) {
            dataList[pos] = item
            notifyItemChanged(pos)
        }
    }

    fun getdayfromDate(dateString: String?): String {
        val mDateFormat = SimpleDateFormat(API_DATE_PATTERN)
        val mCalendar = Calendar.getInstance()
        val mDate = mDateFormat.parse(dateString)
        mCalendar.time = mDate
        return if(mCalendar[Calendar.DAY_OF_MONTH].toString().length<2){
            "0${mCalendar[Calendar.DAY_OF_MONTH]}"
        } else {
            mCalendar[Calendar.DAY_OF_MONTH].toString()
        }
    }
    fun getYearfromDate(dateString: String?): Int {
        val mDateFormat = SimpleDateFormat(API_DATE_PATTERN)
        val mCalendar = Calendar.getInstance()
        val mDate = mDateFormat.parse(dateString)
        mCalendar.time = mDate
        return mCalendar[Calendar.YEAR]
    }

    fun getmonthfromDate(dateString: String?): Int {
        val mDateFormat = SimpleDateFormat(API_DATE_PATTERN, Locale.ENGLISH)
        val mCalendar = Calendar.getInstance()
        val mDate = mDateFormat.parse(dateString)
        mCalendar.time = mDate
        return mCalendar[Calendar.MONTH]
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
    private fun getMonth(monthNum: Int): String {
        val month: Map<Int, String> = mapOf(
            1 to "January",
            2 to "February",
            3 to "March",
            4 to "April",
            5 to "May",
            6 to "June",
            7 to "July",
            8 to "August",
            9 to "September",
            10 to "October",
            11 to "November",
            12 to "December",
        )
        return month[monthNum + 1].toString()
    }

    inner class MultiCityViewHolder(val multiCityBinding: ItemMultiCityReissueBinding) :
        RecyclerView.ViewHolder(multiCityBinding.root)
}

