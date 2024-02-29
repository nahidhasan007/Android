package net.sharetrip.b2b.view.flight.history.vrrvoid.passenger

import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.ReissuePassengerItemsBinding
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApi
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.parseDateForDisplayFromApiFullYear

class PassengerVH(
    private val binding: ReissuePassengerItemsBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun onBind(passenger: ReissueTraveller) {
        binding.passengerName.text = passenger.givenName + " ${passenger.surName}"
        binding.dateOfBirth.text = passenger.dateOfBirth?.parseDateForDisplayFromApiFullYear()
        binding.passengerType.text = passenger.travellerType
        binding.eTicket.text = passenger.eTicket

    }
}