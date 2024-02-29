package net.sharetrip.b2b.view.transaction.view.transactionlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemTransactionListBinding
import net.sharetrip.b2b.view.flight.history.changelist.ChangedFlightHistoryListFragment.Companion.ARGUMENT_UUID_CODE

class TransactionListAdapter :
    RecyclerView.Adapter<TransactionListAdapter.TransactionListViewHolder>() {

    private var transactionList: ArrayList<TransactionUIItemData> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionListViewHolder {
        val binding = ItemTransactionListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionListViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.binding.transaction = transaction

        holder.binding.root.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_transaction_list_to_details,
                bundleOf(ARGUMENT_UUID_CODE to transaction.uuid)
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(list: List<TransactionUIItemData>, isFilter: Boolean) {
        if (isFilter) {
            if (transactionList.isNotEmpty()) {
                transactionList.clear()
                notifyDataSetChanged()
            }
        }
        transactionList.addAll(list)
        notifyItemInserted(transactionList.size)
    }

    override fun getItemCount() = transactionList.size

    class TransactionListViewHolder(val binding: ItemTransactionListBinding) :
        RecyclerView.ViewHolder(binding.root)
}