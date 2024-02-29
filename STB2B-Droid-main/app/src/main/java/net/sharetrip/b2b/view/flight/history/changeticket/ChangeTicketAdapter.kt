package net.sharetrip.b2b.view.flight.history.changeticket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.ItemChangeFlightTicketBinding
import net.sharetrip.b2b.util.DateUtils
import net.sharetrip.b2b.view.flight.booking.model.Flight
import java.util.*


class ChangeTicketAdapter :
    RecyclerView.Adapter<ChangeTicketAdapter.ChangeTicketViewHolder>() {
    var cancellationPossible = false

    private val flightList: ArrayList<Flight> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChangeTicketViewHolder {
        val binding =
            ItemChangeFlightTicketBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ChangeTicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChangeTicketViewHolder, position: Int) {
        holder.binding.flight = flightList[position]
        holder.binding.cancellationPossible = cancellationPossible
        val isCancelable =
            checkCancelAbility(flightList[position].departureDateTime?.date!! + " " + flightList[position].departureDateTime?.time!!)
        holder.binding.isCancelable = isCancelable
    }

    private fun checkCancelAbility(departTime: String): Boolean {
        try {
            if (departTime.isNotEmpty()) {
                val cancellationTime = DateUtils.getDateFormat(departTime)
                val currentTime = DateUtils.getDateFormat((DateUtils.getCurrentDateWithMin()))

                return cancellationTime
                    .after(currentTime) ||
                        cancellationTime == currentTime
            }
        } catch (e: Exception) {

        }
        return false
    }

    fun addItem(list: ArrayList<Flight>, cancellationPossible: Boolean) {
        flightList.clear()
        flightList.addAll(list)
        notifyItemInserted(flightList.size)
        this.cancellationPossible = cancellationPossible
    }

    override fun getItemCount() = flightList.size

    class ChangeTicketViewHolder(val binding: ItemChangeFlightTicketBinding) :
        RecyclerView.ViewHolder(binding.root)

}