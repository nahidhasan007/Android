package net.sharetrip.b2b.view.flight.history.downloadvowcher

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.ItemCustomPriceBinding
import net.sharetrip.b2b.util.CustomTextWatcher
import net.sharetrip.b2b.view.flight.booking.model.PriceBreakdown
import net.sharetrip.b2b.view.flight.history.model.TravellerItem

class CustomerPricingAdapter(private val viewModel: DownloadVoucherVM) :
    ListAdapter<PriceBreakdown, CustomerPricingAdapter.CustomerPricingViewHolder>(
        PricingDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerPricingViewHolder =
        CustomerPricingViewHolder(
            ItemCustomPriceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CustomerPricingViewHolder, position: Int) {
        holder.itemCustomPriceBinding.price = getItem(position)
        holder.itemCustomPriceBinding.position = position
        var baseFare: Double = getItem(position).baseFare * getItem(position).numberPaxes
        var taxFare: Double = getItem(position).tax * getItem(position).numberPaxes
        holder.itemCustomPriceBinding.totalPrice =
            baseFare + taxFare

        holder.itemCustomPriceBinding.editTextBaseFare.addTextChangedListener(object :
            CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                baseFare = if (s.isNotEmpty()) {
                    s.toString().toDouble()
                } else {
                    0.0
                }

                viewModel.addCustomPrice(
                    TravellerItem(baseFare, taxFare),
                    getItem(position).type
                )
                holder.itemCustomPriceBinding.totalPrice = baseFare + taxFare
            }
        })

        holder.itemCustomPriceBinding.editTextTax.addTextChangedListener(object :
            CustomTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                taxFare = if (s.isNotEmpty()) {
                    s.toString().toDouble()
                } else {
                    0.0
                }

                viewModel.addCustomPrice(
                    TravellerItem(baseFare, taxFare),
                    getItem(position).type
                )
                holder.itemCustomPriceBinding.totalPrice = baseFare + taxFare
            }
        })
    }

    inner class CustomerPricingViewHolder(val itemCustomPriceBinding: ItemCustomPriceBinding) :
        RecyclerView.ViewHolder(itemCustomPriceBinding.root)

    private class PricingDiffCallback : DiffUtil.ItemCallback<PriceBreakdown>() {
        override fun areItemsTheSame(oldItem: PriceBreakdown, newItem: PriceBreakdown): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: PriceBreakdown, newItem: PriceBreakdown): Boolean {
            return oldItem == newItem
        }
    }
}