package net.sharetrip.b2b.view.flight.history.reissuepassengerdetails

import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.ReissuePassengerItemsBinding
import net.sharetrip.b2b.view.flight.history.model.TravellerX
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApi
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApiFullYear

class ReissuePassengerViewHolder(
    private val binding: ReissuePassengerItemsBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun onBind(passenger: TravellerX) {
        binding.passengerName.text = passenger.name
        binding.dateOfBirth.text = passenger.dateOfBirth.parseDateForDisplayFromApiFullYear()
        binding.email.text = passenger.email
        binding.emdTicket.text = passenger.emdTicket
        binding.eTicket.text = passenger.eTicket
        binding.mobileNo.text = passenger.mobileNumber
        binding.passportNo.text = passenger.passportNumber
        binding.passengerType.text = passenger.paxType

    }
}