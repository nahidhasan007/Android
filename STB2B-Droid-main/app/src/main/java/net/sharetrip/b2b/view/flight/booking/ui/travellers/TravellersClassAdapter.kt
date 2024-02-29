package net.sharetrip.b2b.view.flight.booking.ui.travellers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemTravelClassBinding

class TravellersClassAdapter(private val classList: List<String>, val viewModel: TravellersVM) :
    RecyclerView.Adapter<TravellersClassAdapter.TravellersClassHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravellersClassHolder {
        val viewHolder = DataBindingUtil.inflate<ItemTravelClassBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_travel_class, parent, false
        )
        return TravellersClassHolder(viewHolder)
    }

    override fun getItemCount(): Int = classList.size

    override fun onBindViewHolder(holder: TravellersClassHolder, position: Int) {
        val className: String = classList[position]

        holder.binding.className = className
        holder.binding.buttonTravelClass.checked(className == viewModel.travellers.classType)
        holder.binding.buttonTravelClass.setOnClickListener() {
            viewModel.travellers.classType = className
            notifyDataSetChanged()
        }
    }

    class TravellersClassHolder(val binding: ItemTravelClassBinding) :
        RecyclerView.ViewHolder(binding.root)
}
