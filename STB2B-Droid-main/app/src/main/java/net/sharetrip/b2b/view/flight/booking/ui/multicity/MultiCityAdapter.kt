package net.sharetrip.b2b.view.flight.booking.ui.multicity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemMultiCityBinding

class MultiCityAdapter : RecyclerView.Adapter<MultiCityAdapter.MultiCityViewHolder>() {
    private lateinit var mListener: MultiCityItemClickListener

    fun setMultiCityItemClickListener(mListener: MultiCityItemClickListener) {
        this.mListener = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiCityViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cityBinding = DataBindingUtil.inflate<ItemMultiCityBinding>(
            inflater,
            R.layout.item_multi_city, parent, false
        )
        return MultiCityViewHolder(cityBinding)
    }

    override fun onBindViewHolder(holder: MultiCityViewHolder, position: Int) {
        val multiCityModel = MultiCityFragment.multiCityList[position]
        if (multiCityModel.origin.isEmpty() && position > 0)
            multiCityModel.origin = MultiCityFragment.multiCityList[position - 1].destination
        holder.multiCityBinding.data = multiCityModel
        holder.bindingListener()
    }

    inner class MultiCityViewHolder(val multiCityBinding: ItemMultiCityBinding) :
        RecyclerView.ViewHolder(multiCityBinding.root) {

        fun bindingListener() {
            multiCityBinding.layoutFrom.setOnClickListener {
                mListener.onFromItemClick(adapterPosition)
            }

            multiCityBinding.layoutTo.setOnClickListener { mListener.onToItemClick(adapterPosition) }

            multiCityBinding.layoutDeparture.setOnClickListener {
                mListener.onDateItemClick(
                    adapterPosition,
                    multiCityBinding.textViewFromCode.text.toString(),
                    multiCityBinding.textViewToCode.text.toString()
                )
            }

            multiCityBinding.layoutSwapCity.setOnClickListener {
                mListener.onSwapAirportClick(
                    adapterPosition,
                    multiCityBinding.textViewFromCode.text.toString(),
                    multiCityBinding.textViewToCode.text.toString()
                )
            }
        }
    }

    interface MultiCityItemClickListener {

        fun onFromItemClick(mPosition: Int)

        fun onToItemClick(mPosition: Int)

        fun onDateItemClick(mPosition: Int, origin: String, destination: String)

        fun onSwapAirportClick(mPosition: Int, origin: String, destination: String)
    }

    override fun getItemCount() = MultiCityFragment.multiCityList.size
}
