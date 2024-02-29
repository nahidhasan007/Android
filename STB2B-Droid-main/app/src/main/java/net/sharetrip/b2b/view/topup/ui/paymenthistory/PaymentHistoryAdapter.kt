package net.sharetrip.b2b.view.topup.ui.paymenthistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemPaymentHistoryBinding
import net.sharetrip.b2b.view.topup.model.PaymentHistory
import net.sharetrip.b2b.view.topup.ui.historydetails.HistoryDetailsFragment

class PaymentHistoryAdapter : RecyclerView.Adapter<PaymentHistoryAdapter.HistoryHolder>() {

    private val historyList: ArrayList<PaymentHistory> = arrayListOf()

    class HistoryHolder(val itemPaymentHistoryBinding: ItemPaymentHistoryBinding) :
        RecyclerView.ViewHolder(itemPaymentHistoryBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val viewHolder = ItemPaymentHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HistoryHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        holder.itemPaymentHistoryBinding.paymentHistory = historyList[position]

        holder.itemPaymentHistoryBinding.root.setOnClickListener {
            val bundle =
                bundleOf(HistoryDetailsFragment.ARG_HISTORY_DETAILS to historyList[position])
            it.findNavController()
                .navigate(R.id.action_payment_dest_to_payment_history_details, bundle)
        }
    }

    fun addItem(datum: ArrayList<PaymentHistory>) {
        historyList.addAll(datum)
        notifyItemInserted(historyList.size)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}