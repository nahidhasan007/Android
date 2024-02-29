package net.sharetrip.b2b.view.flight.history.passenger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.ItemSelectPassengerBinding
import net.sharetrip.b2b.view.flight.history.model.Traveller
import java.util.*

class SelectPassengerAdapter(
    var travellers: ArrayList<Traveller>,
    private val selectPassengerVM: SelectPassengerVM
) :
    RecyclerView.Adapter<SelectPassengerAdapter.SelectPassengerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectPassengerViewHolder {
        val binding =
            ItemSelectPassengerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectPassengerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectPassengerViewHolder, position: Int) {
        holder.binding.traveller = travellers[position]

        holder.binding.switchFilter.setOnClickListener {
            travellers[position].isChecked = !travellers[position].isChecked
            selectPassengerVM.isAllSelected(position, travellers[position].isChecked)
        }
    }

    fun resetItems(travellersLIst: ArrayList<Traveller>) {
        travellers = travellersLIst
        notifyDataSetChanged()

    }

    override fun getItemCount() = travellers.size

    class SelectPassengerViewHolder(val binding: ItemSelectPassengerBinding) :
        RecyclerView.ViewHolder(binding.root)
}