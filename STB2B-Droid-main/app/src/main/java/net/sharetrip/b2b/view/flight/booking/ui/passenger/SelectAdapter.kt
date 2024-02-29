package net.sharetrip.b2b.view.flight.booking.ui.passenger

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.ItemOptionBinding
import net.sharetrip.b2b.view.flight.booking.model.SpecialServiceRequest
import net.sharetrip.b2b.view.flight.booking.ui.ListItemClickListener

class SelectAdapter(
    private val serviceList: List<SpecialServiceRequest>,
    private var code: String,
    private val listener: ListItemClickListener<SpecialServiceRequest>
) : RecyclerView.Adapter<SelectAdapter.OptionHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
        val viewHolder = ItemOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return OptionHolder(viewHolder)
    }

    override fun getItemCount(): Int = serviceList.size

    override fun onBindViewHolder(holder: OptionHolder, position: Int) {
        val option = serviceList[position]
        holder.bindingItem.option = option.name
        holder.bindingItem.btnSelection.checked(code == serviceList[position].code)

        holder.bindingItem.btnSelection.setOnClickListener {
            listener.onClickItem(serviceList[position])
        }
    }

    class OptionHolder(val bindingItem: ItemOptionBinding) :
        RecyclerView.ViewHolder(bindingItem.root)
}
