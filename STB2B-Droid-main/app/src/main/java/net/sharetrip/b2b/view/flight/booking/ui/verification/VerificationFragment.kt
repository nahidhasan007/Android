package net.sharetrip.b2b.view.flight.booking.ui.verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentVerificationBinding
import net.sharetrip.b2b.localdb.LocalDataBase
import net.sharetrip.b2b.util.DateUtils.calculatePassengerAge
import net.sharetrip.b2b.util.MsgUtils.contactInfoMsg
import net.sharetrip.b2b.util.getNavigationResultLiveData
import net.sharetrip.b2b.view.flight.booking.model.ContactInfo
import net.sharetrip.b2b.view.flight.booking.model.Passenger
import net.sharetrip.b2b.view.flight.booking.ui.flightdetails.FlightDetailsFragment

class VerificationFragment : Fragment() {
    var ageVerificationMsg: String = ""
    private val viewModel: VerificationVM by lazy {
        val passengerDao = LocalDataBase.getDataBase(requireContext()).passengerDao()
        val userDao = LocalDataBase.getDataBase(requireContext()).userProfileDao()
        val flightSearchDao = LocalDataBase.getDataBase(requireContext()).flightSearchDao()
        VerificationVMFactory(VerificationRepo(passengerDao, userDao, flightSearchDao)).create(
            VerificationVM::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentVerificationBinding.inflate(inflater, container, false)
        bindingView.viewModel = viewModel
        val isDomestic = arguments?.getBoolean(FlightDetailsFragment.ARG_IS_DOMESTIC)!!

        bindingView.btnContinue.setOnClickListener {
            if (ageVerificationMsg.isEmpty()) {
                val contactInfo = viewModel.contactInfo?.get()
                if (contactInfo!=null && contactInfo.isValid()) {
                    findNavController().navigate(
                        R.id.action_verification_to_booking_summary,
                        bundleOf(CONTACT_VERIFICATION to viewModel.contactInfo.get())
                    )
                } else {
                    Toast.makeText(requireContext(), contactInfoMsg, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), ageVerificationMsg, Toast.LENGTH_SHORT).show()
            }
        }

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val adapter = VerificationAdapter(isDomestic)
        bindingView.listPassenger.adapter = adapter

        viewModel.passengerList.observe(viewLifecycleOwner) { passengers ->
            passengers.let {
                adapter.submitList(it.first)
                ageVerification(it.first, it.second)
            }
        }

        getNavigationResultLiveData<ContactInfo>(KEY_CONTACT)?.observe(viewLifecycleOwner) {
            viewModel.contactInfo.set(it)
        }

        bindingView.layoutContactInfo.labelEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_passenger_to_update_contact_sheet,
                bundleOf(CONTACT_VERIFICATION to viewModel.contactInfo.get())
            )
        }

        return bindingView.root
    }

    private fun ageVerification(passengers: List<Passenger>, flightDate: String) {
        for (passenger in passengers) {
            val age = calculatePassengerAge(passenger.dateOfBirth, flightDate)

            if (passenger.id.contains("Child")) {
                if (age > 11 || age < 2) {
                    ageVerificationMsg =
                        getString(R.string.child_age_verification_msg, passenger.id)
                    break
                }
            } else if (passenger.id.contains("Infant")) {
                if (age > 2) {
                    ageVerificationMsg =
                        getString(R.string.infant_age_verification_msg, passenger.id)
                    break
                }
            }
        }

    }
}
