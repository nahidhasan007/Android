package net.sharetrip.b2b.view.flight.booking.ui.passengerlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemPassengerBinding
import net.sharetrip.b2b.view.flight.booking.model.Passenger
import net.sharetrip.b2b.view.flight.booking.ui.flightdetails.FlightDetailsFragment
import net.sharetrip.b2b.view.flight.booking.ui.passenger.KEY_PASSENGER_ID

class PassengerAdapter(private val isDomestic: Boolean) :
    ListAdapter<Passenger, PassengerAdapter.PassengerViewHolder>(PassengerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengerViewHolder =
        PassengerViewHolder(
            ItemPassengerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: PassengerViewHolder, position: Int) {
        val passenger = getItem(position)
        if (passenger.lastName.isEmpty()) {
            holder.itemPassengerBinding.passenger = passenger.id
        } else {
            holder.itemPassengerBinding.passenger = passenger.lastName
        }

        holder.itemPassengerBinding.textViewPassenger.setOnClickListener() { view ->
            val bundle = bundleOf(
                KEY_PASSENGER_ID to passenger.id,
                FlightDetailsFragment.ARG_IS_DOMESTIC to isDomestic
            )
            view.findNavController().navigate(R.id.action_passenger_list_to_details, bundle)
        }
    }

    inner class PassengerViewHolder(val itemPassengerBinding: ItemPassengerBinding) :
        RecyclerView.ViewHolder(itemPassengerBinding.root)

    private class PassengerDiffCallback : DiffUtil.ItemCallback<Passenger>() {
        override fun areItemsTheSame(oldItem: Passenger, newItem: Passenger): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Passenger, newItem: Passenger): Boolean {
            return oldItem == newItem
        }
    }
}
