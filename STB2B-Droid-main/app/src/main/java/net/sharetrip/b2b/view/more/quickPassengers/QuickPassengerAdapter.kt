package net.sharetrip.b2b.view.more.quickPassengers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.ItemQuickPassengerBinding
import net.sharetrip.b2b.view.flight.booking.ui.ListItemClickListener
import net.sharetrip.b2b.view.more.model.QuickPassenger

class QuickPassengerAdapter(var listener: ListItemClickListener<QuickPassenger>) :
    RecyclerView.Adapter<QuickPassengerAdapter.OptionHolder>() {

    var passengerList: MutableList<QuickPassenger> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
        val viewHolder = ItemQuickPassengerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return OptionHolder(viewHolder)
    }

    override fun getItemCount() = passengerList.size

    override fun onBindViewHolder(holder: OptionHolder, position: Int) {
        val option = passengerList[position]
        holder.bindingItem.textViewQuickPassenger.text = option.firstName + " " + option.lastName

        holder.bindingItem.textViewQuickPassenger.setOnClickListener {
            listener.onClickItem(passengerList[position])
        }
    }

    fun addPassengerList(list: List<QuickPassenger>) {
        passengerList.clear()
        passengerList.addAll(list)
        notifyDataSetChanged()
    }

    class OptionHolder(val bindingItem: ItemQuickPassengerBinding) :
        RecyclerView.ViewHolder(bindingItem.root)
}