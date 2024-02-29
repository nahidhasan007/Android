package net.sharetrip.b2b.view.flight.booking.ui.flightlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemAirlinesLayoutBinding
import net.sharetrip.b2b.view.flight.booking.model.Airline

class AirlinesAdapter(private val listener: AirlinesListener) :
    RecyclerView.Adapter<AirlinesAdapter.AirlinesHolder>() {

    private var airlineList = listOf<Airline>()
    var selectedCode = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirlinesHolder {
        val viewHolder = DataBindingUtil.inflate<ItemAirlinesLayoutBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_airlines_layout, parent, false
        )
        return AirlinesHolder(viewHolder)
    }

    override fun getItemCount() = airlineList.size

    fun addAirlinesList(list: List<Airline>) {
        this.airlineList = list
    }

    override fun onBindViewHolder(holder: AirlinesHolder, position: Int) {
        val airline = airlineList[position]
        holder.binding.viewModel = airline
        holder.binding.selectedCode = selectedCode
        holder.binding.root.setOnClickListener {
            listener.onCodeSelect(airline.code!!)
            selectedCode = airline.code
            notifyDataSetChanged()
        }
    }

    class AirlinesHolder(val binding: ItemAirlinesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}

interface AirlinesListener {
    fun onCodeSelect(code: String)
}