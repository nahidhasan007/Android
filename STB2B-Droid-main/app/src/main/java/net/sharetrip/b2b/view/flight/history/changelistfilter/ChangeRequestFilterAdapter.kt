package net.sharetrip.b2b.view.flight.history.changelistfilter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemChangedFlightFilterBinding

class ChangeRequestFilterAdapter(
    private val dataList: List<String>,
    var itemClickListener: ChangedFilterOnItemClick,
    private var lastSelectedData: String = ""
) : RecyclerView.Adapter<ChangeRequestFilterAdapter.ChangeRequestViewHolder>() {

    var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeRequestViewHolder {
        val viewHolder = DataBindingUtil.inflate<ItemChangedFlightFilterBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_changed_flight_filter, parent, false
        )
        return ChangeRequestViewHolder(viewHolder)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: ChangeRequestViewHolder, position: Int) {
        val className: String = dataList[position]
        holder.binding.filterRequest = className
        holder.binding.buttonTravelClass.checked(lastSelectedData == className)
        holder.binding.buttonTravelClass.setOnClickListener {
            lastSelectedData = className
            itemClickListener.selectRequest(className)

            val previousSelectedPosition = selectedPosition
            selectedPosition = position

            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(position)
        }
    }

    class ChangeRequestViewHolder(val binding: ItemChangedFlightFilterBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface ChangedFilterOnItemClick {
        fun selectRequest(data: String)
    }
}