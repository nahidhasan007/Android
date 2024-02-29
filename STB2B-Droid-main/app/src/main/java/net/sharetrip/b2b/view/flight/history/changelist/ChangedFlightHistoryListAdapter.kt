package net.sharetrip.b2b.view.flight.history.changelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemFlightTicketChangeListBinding
import net.sharetrip.b2b.view.flight.history.changelist.ChangedFlightHistoryListFragment.Companion.ARGUMENT_UUID_CODE
import net.sharetrip.b2b.view.flight.history.model.ChangeTicket

class ChangedFlightHistoryListAdapter :
    RecyclerView.Adapter<ChangedFlightHistoryListAdapter.ChangeTicketListViewHolder>() {

    private var changeTicketList: ArrayList<ChangeTicket> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeTicketListViewHolder {
        val binding =
            ItemFlightTicketChangeListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ChangeTicketListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChangeTicketListViewHolder, position: Int) {
        val changeTicket = changeTicketList[position]
        holder.binding.changeTicketDetails = changeTicket

        holder.binding.root.setOnClickListener { view ->
            view.findNavController().navigate(
                R.id.action_to_changed_flight_details,
                bundleOf(ARGUMENT_UUID_CODE to changeTicket.uuid)
                // N.B: TO CHECK REFUND USE BELOW CODE
                //"REFUND-B63C5655F6934"
            )
        }
    }

    fun addItems(list: List<ChangeTicket>, isFilter: Boolean) {
        if (isFilter) {
            if (changeTicketList.isNotEmpty()){
                changeTicketList.clear()
                notifyDataSetChanged()
            }
        }

        changeTicketList.addAll(list)
        notifyItemInserted(changeTicketList.size)
    }

    override fun getItemCount() = changeTicketList.size

    class ChangeTicketListViewHolder(val binding: ItemFlightTicketChangeListBinding) :
        RecyclerView.ViewHolder(binding.root)
}