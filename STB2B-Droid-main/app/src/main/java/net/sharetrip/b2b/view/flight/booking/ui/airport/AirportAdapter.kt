package net.sharetrip.b2b.view.flight.booking.ui.airport

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.ItemAirportBinding
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.view.flight.booking.model.Airport

class AirportAdapter(private val airportSearchVM: AirportSearchVM) :
    ListAdapter<Airport, AirportAdapter.AirportViewHolder>(AirportDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirportViewHolder =
        AirportViewHolder(
            ItemAirportBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: AirportViewHolder, position: Int) {
        try {
            val airport = getItem(position)
            holder.itemAirportBinding.airport = airport
            holder.itemAirportBinding.root.setOnClickListener {
                airportSearchVM.updateAirports(airport)
            }
        } catch (exception: Exception) {
            airportSearchVM._showMessage.value = MsgUtils.unKnownErrorMsg
        }
    }

    inner class AirportViewHolder(val itemAirportBinding: ItemAirportBinding) :
        RecyclerView.ViewHolder(itemAirportBinding.root)

    private class AirportDiffCallback : DiffUtil.ItemCallback<Airport>() {
        override fun areItemsTheSame(oldItem: Airport, newItem: Airport): Boolean {
            return oldItem.iata == newItem.iata
        }

        override fun areContentsTheSame(oldItem: Airport, newItem: Airport): Boolean {
            return oldItem == newItem
        }
    }
}
