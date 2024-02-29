package net.sharetrip.b2b.view.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentBookingBinding

class BookingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentBookingBinding.inflate(layoutInflater, container, false)

        bindingView.buttonFlight.setOnClickListener{
            findNavController().navigate(R.id.action_booking_to_flight_history)
        }

        bindingView.buttonVoidRefundChange.setOnClickListener{
            findNavController().navigate(R.id.action_booking_to_flight_history_change_ticket_list)
        }

        return bindingView.root
    }
}