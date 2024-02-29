package net.sharetrip.b2b.view.flight.history.reissuepassengerdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ReissuePassengerItemsBinding
import net.sharetrip.b2b.view.flight.history.model.TravellerX

class ReissuePassengerAdapter(
    private val passenger: List<TravellerX> = listOf(),
) : RecyclerView.Adapter<ReissuePassengerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReissuePassengerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ReissuePassengerItemsBinding>(
            inflater,
            R.layout.reissue_passenger_items,
            parent,
            false
        )
        return ReissuePassengerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReissuePassengerViewHolder, position: Int) {
        holder.onBind(passenger[position])
    }

    override fun getItemCount(): Int {
        return passenger.size
    }


}