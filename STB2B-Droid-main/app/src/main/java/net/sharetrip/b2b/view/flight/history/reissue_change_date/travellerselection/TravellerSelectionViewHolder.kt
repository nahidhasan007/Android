package net.sharetrip.b2b.view.flight.history.reissue_change_date.travellerselection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.TravellerCheckboxItemBinding
import net.sharetrip.b2b.view.flight.history.model.Traveller
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller

class TravellerSelectionViewHolder(
    private val binding: TravellerCheckboxItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun onBind(
        traveller: ReissueTraveller,
        checkboxClickItem: (position: Int, check: Boolean) -> Unit,
        isSelectAllTraveller: Boolean
    ) {
        binding.checkBox.text = traveller.givenName + " ${traveller.surName}"
        if (isSelectAllTraveller) {
            binding.checkBox.isChecked = true
        }
        binding.checkBox.isChecked = traveller.isChecked
        binding.ticket.text = "eTicket: ${traveller.eTicket}"
        binding.checkBox.setOnCheckedChangeListener { _, b ->
            checkboxClickItem(bindingAdapterPosition, b)
        }
    }

    companion object {
        fun create(parent: ViewGroup) = TravellerSelectionViewHolder(
            TravellerCheckboxItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}