package net.sharetrip.b2b.view.flight.history.passengerdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.databinding.FragmentPassengerDetailsBinding
import net.sharetrip.b2b.view.flight.booking.model.ContactInfo
import net.sharetrip.b2b.view.flight.booking.ui.flightdetails.FlightDetailsFragment
import net.sharetrip.b2b.view.flight.history.model.Traveller

class PassengerDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentPassengerDetailsBinding.inflate(layoutInflater, container, false)
        val passengers: ArrayList<Traveller> =
            arguments?.getParcelableArrayList(ARGS_PASSENGER_DETAILS)!!

        bindingView.contactInfo = passengers[0].mobileNumber?.let {
            passengers[0].email?.let { it1 ->
                ContactInfo(
                    it,
                    it1
                )
            }
        }
        val adapter =
            PassengerDetailsAdapter(requireArguments().getBoolean(FlightDetailsFragment.ARG_IS_DOMESTIC))
        bindingView.listPassenger.adapter = adapter
        adapter.submitList(passengers)

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return bindingView.root
    }

    companion object {
        const val ARGS_PASSENGER_DETAILS = "passenger_details"
    }
}