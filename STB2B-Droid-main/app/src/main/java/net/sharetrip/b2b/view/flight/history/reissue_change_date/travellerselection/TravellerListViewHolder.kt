package net.sharetrip.b2b.view.flight.history.reissue_change_date.travellerselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.TravellerItemBinding
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller
import java.text.FieldPosition

class TravellerListViewHolder(
    private val binding: TravellerItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun travellerBind(
        traveller: ReissueTraveller,
        position: Int
    ) {
        binding.nameText.text = "${position+1}: ${traveller.givenName}" + " ${traveller.surName}"
        binding.ticket.text = "eTicket: ${traveller.eTicket}"
    }

    companion object {
        fun create(parent: ViewGroup) = TravellerListViewHolder(
            TravellerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}