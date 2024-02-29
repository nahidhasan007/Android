package net.sharetrip.b2b.view.flight.history.vrrvoid.travellerselection

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.TravellerRefundCheckboxItemBinding
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller

class VoidTravellerVH(
    private val binding: TravellerRefundCheckboxItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun onBind(
        traveller: ReissueTraveller,
        checkboxClickItem: (position: Int, check: Boolean) -> Unit,
    ) {
        binding.checkBox.text = traveller.givenName+traveller.surName
//        if (isSelectAllTraveller) {
//            binding.checkBox.isChecked = true
//        }
        binding.checkBox.isChecked = traveller.isChecked
        binding.ticket.text = "eTicket: ${traveller.eTicket}"
        binding.checkBox.setOnCheckedChangeListener { _, b ->
            checkboxClickItem(bindingAdapterPosition, b)
        }
    }

    companion object {
        fun create(parent: ViewGroup) = VoidTravellerVH(
            TravellerRefundCheckboxItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}