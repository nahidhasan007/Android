package net.sharetrip.b2b.view.flight.history.vrrvoid.passenger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ReissuePassengerItemsBinding
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller

class PassengerAdapter(
    private val passenger: List<ReissueTraveller> = listOf(),
) : RecyclerView.Adapter<PassengerVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengerVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ReissuePassengerItemsBinding>(
            inflater,
            R.layout.reissue_passenger_items,
            parent,
            false
        )
        return PassengerVH(binding)
    }

    override fun onBindViewHolder(holder: PassengerVH, position: Int) {
        holder.onBind(passenger[position])
    }

    override fun getItemCount(): Int {
        return passenger.size
    }


}