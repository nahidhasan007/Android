package net.sharetrip.b2b.view.flight.booking.ui.flightdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.ItemFlightDetailsBinding
import net.sharetrip.b2b.view.flight.booking.model.Flight

class FlightDetailsAdapter(val flights: List<Flight>, val listener: ItemClickListener) :
    RecyclerView.Adapter<FlightDetailsAdapter.FlightDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightDetailsViewHolder {
        val binding =
            ItemFlightDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlightDetailsViewHolder(binding)
    }

    override fun getItemCount(): Int = flights.size

    override fun onBindViewHolder(holder: FlightDetailsViewHolder, position: Int) {
        holder.binding.flight = flights[position]

        holder.binding.textViewSeeDetails.setOnClickListener() {
            listener.onClickItem(flights[position],position)
        }
    }

    class FlightDetailsViewHolder(val binding: ItemFlightDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface ItemClickListener {
        fun onClickItem(flight: Flight,position: Int)
    }
}
