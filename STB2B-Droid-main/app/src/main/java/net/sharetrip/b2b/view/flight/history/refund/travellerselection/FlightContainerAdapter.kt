package net.sharetrip.b2b.view.flight.history.refund.travellerselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.sharetrip.b2b.databinding.ItemFlightInfoBinding
import net.sharetrip.b2b.view.flight.history.model.Flight

class FlightContainerAdapter(private val flights: List<Flight>): RecyclerView.Adapter<FlightContainerAdapter.FlightContainerVh>() {

    companion object {
        const val TAG = "FlightContainerAdapter"
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightContainerVh {
        return FlightContainerVh.create(parent)
    }

    override fun getItemCount(): Int {
        return flights.size
    }

    override fun onBindViewHolder(holder: FlightContainerVh, position: Int) {
        holder.onBind(flights[position])
    }


    class FlightContainerVh(private val binding: ItemFlightInfoBinding): RecyclerView.ViewHolder (binding.root) {
        companion object {
            fun create(parent: ViewGroup) = FlightContainerVh(
                ItemFlightInfoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        fun onBind(flight: Flight) {
            Glide.with(binding.airlineLogo).load(flight.logo).into(binding.airlineLogo)
            binding.originDestCode.text = flight.originCode
            binding.departureDayTime.text = flight.departureDate
            binding.arrivalDayTime.text = flight.arrivalDate
            binding.duration.text = flight.flyDuration


            binding.airlineName.text = flight.airline
            binding.departureTime.text = flight.departureTime
            binding.arrivalTime.text = flight.arrivalTime
            binding.flightNumber.text = flight.flightNumber
        }
    }

}