package net.sharetrip.b2b.view.flight.history.historylist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ItemFlightHistoryBinding
import net.sharetrip.b2b.view.flight.history.historylist.FlightHistoryListFragment.Companion.FLIGHT_HISTORY_DETAILS
import net.sharetrip.b2b.view.flight.history.model.FlightHistory

class FlightHistoryAdapter :
    RecyclerView.Adapter<FlightHistoryAdapter.FlightDetailsViewHolder>() {
    private val historyList: ArrayList<FlightHistory> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightDetailsViewHolder {
        val binding =
            ItemFlightHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlightDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlightDetailsViewHolder, position: Int) {
        holder.binding.flightHistory = historyList[position]
        holder.binding.root.setOnClickListener { view ->
            Log.d("ModifyHistorySize", historyList[position].modificationHistories?.size.toString())
            if (historyList[position].modificationHistories?.size==0) {
                view.findNavController().navigate(
                    R.id.action_flight_history_dest_to_history_details_dest,
                    bundleOf(FLIGHT_HISTORY_DETAILS to historyList[position])

                )
            } else {
                view.findNavController().navigate(
                    R.id.action_flight_history_dest_to_bookingHistoryDetailsFragment,
                    bundleOf(FLIGHT_HISTORY_DETAILS to historyList[position])
                )
            }
        }
    }

    fun addItem(history: ArrayList<FlightHistory>) {
        historyList.addAll(history)
        notifyItemInserted(historyList.size)
    }

    override fun getItemCount(): Int = historyList.size

    class FlightDetailsViewHolder(val binding: ItemFlightHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}