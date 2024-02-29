package net.sharetrip.b2b.view.flight.history.reissue_change_date.flightselection

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.util.dateChangeToHourDDMMYYYYFromT
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlight
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.dateChangeToHourWeekdayFromT
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApi

class FlightSelectionAdapter(
    var flightList: List<ReissueFlight>,
    val isFullJourneySelect: Boolean,
    val checkboxClickItem: (position: Int, check: Boolean) -> Unit
) : RecyclerView.Adapter<FlightSelectionAdapter.Viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.flight_checkbox_item, parent, false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return flightList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {

//        if (flightList[position].isFlightSelected) {
//            holder.checkBox.isChecked = true
//        }
//
//        if (isFullJourneySelect) {
//            holder.checkBox.isEnabled = false
//        } else {
//            holder.checkBox.setOnCheckedChangeListener { _, b ->
//                checkboxClickItem(position, b)
//            }
//        }

        holder.beginFrom.text = flightList[position].origin?.city
        holder.endDesTo.text = flightList[position].destination?.city
        holder.beginAirport.text =
            flightList[position].origin?.code + "," + flightList[position].origin?.airport
        holder.endAirport.text =
            flightList[position].destination?.code + "," + flightList[position].destination?.airport
        holder.flightTime.text =
            flightList[position].departure?.dateTime?.dateChangeToHourWeekdayFromT()
        holder.flightDate.text =
            flightList[position].departure?.dateTime?.parseDateForDisplayFromApi()

    }


    inner class Viewholder(view: View) : RecyclerView.ViewHolder(view) {

//        val checkBox: CheckBox
        val beginFrom: TextView
        val endDesTo: TextView
        val beginAirport: TextView
        val endAirport: TextView
        val flightTime: TextView
        val flightDate: TextView

        init {
//            checkBox = view.findViewById(R.id.checkBox)
            beginFrom = view.findViewById(R.id.locationFrom)
            endDesTo = view.findViewById(R.id.locationTo)
            beginAirport = view.findViewById(R.id.airportFrom)
            endAirport = view.findViewById(R.id.airportTo)
            flightTime = view.findViewById(R.id.journeyDayTime)
            flightDate = view.findViewById(R.id.date)
        }
    }
}
