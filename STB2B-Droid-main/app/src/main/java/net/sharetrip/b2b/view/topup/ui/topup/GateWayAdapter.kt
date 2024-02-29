package net.sharetrip.b2b.view.topup.ui.topup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.ItemPaymentMethodBinding
import net.sharetrip.b2b.view.flight.booking.ui.ListItemClickListener
import net.sharetrip.b2b.view.topup.model.GateWay

class GateWayAdapter(private val listener: ListItemClickListener<GateWay>) :
    ListAdapter<GateWay, GateWayAdapter.TopUpViewHolder>(TopUpDiffCallback()) {
    var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopUpViewHolder =
        TopUpViewHolder(
            ItemPaymentMethodBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: TopUpViewHolder, position: Int) {
        if (position == selectedPosition) {
            listener.onClickItem(getItem(position))
        }
        getItem(position).isSelected = position == selectedPosition

        holder.itemPaymentMethodBinding.gateWay = getItem(position)
        holder.itemPaymentMethodBinding.root.setOnClickListener {
            val previousSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(position)
        }
    }

    inner class TopUpViewHolder(val itemPaymentMethodBinding: ItemPaymentMethodBinding) :
        RecyclerView.ViewHolder(itemPaymentMethodBinding.root)

    private class TopUpDiffCallback : DiffUtil.ItemCallback<GateWay>() {
        override fun areItemsTheSame(oldItem: GateWay, newItem: GateWay): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GateWay, newItem: GateWay): Boolean {
            return oldItem == newItem
        }
    }
}