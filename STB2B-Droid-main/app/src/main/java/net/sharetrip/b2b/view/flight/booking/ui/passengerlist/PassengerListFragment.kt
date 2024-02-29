package net.sharetrip.b2b.view.flight.booking.ui.passengerlist

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
import net.sharetrip.b2b.databinding.FragmentPassengerListBinding
import net.sharetrip.b2b.localdb.LocalDataBase
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.view.flight.booking.ui.flightdetails.FlightDetailsFragment

class PassengerListFragment : Fragment() {
    var isDomestic: Boolean = false
    private val viewModel: PassengerListVM by lazy {
        val passengerDao = LocalDataBase.getDataBase(requireContext()).passengerDao()
        isDomestic = arguments?.getBoolean(FlightDetailsFragment.ARG_IS_DOMESTIC)!!
        PassengerListVMFactory(
            PassengerListRepo((passengerDao)),
            isDomestic
        ).create(PassengerListVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentPassengerListBinding.inflate(inflater, container, false)
        bindingView.viewModel = viewModel
        val adapter = PassengerAdapter(isDomestic)
        bindingView.listPassenger.adapter = adapter

        viewModel.passengerList.observe(viewLifecycleOwner) { passengers ->
            passengers.let {
                adapter.submitList(it)
            }
        }

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.moveToNext.observe(viewLifecycleOwner) {
            if (it) findNavController().navigate(
                R.id.action_passenger_list_to_verification, bundleOf(
                    FlightDetailsFragment.ARG_IS_DOMESTIC to isDomestic
                )
            )
        }

        viewModel.showMessage.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(
                requireContext(),
                getString(R.string.fill_up_required_field),
                Toast.LENGTH_LONG
            ).show()
        })

        return bindingView.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.updatePassengerList()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
}
