package net.sharetrip.b2b.view.flight.history.passenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentFlightHistorySelectPassengerBinding
import net.sharetrip.b2b.util.toTravellerList
import net.sharetrip.b2b.util.travellersListToString
import net.sharetrip.b2b.view.flight.history.historydetails.FlightHistoryDetailsFragment
import net.sharetrip.b2b.view.flight.history.model.FlightHistory
import net.sharetrip.b2b.view.flight.history.model.Traveller
import java.util.*

class SelectPassengerFragment : Fragment() {
    val flightHistory: FlightHistory by lazy {
        arguments?.getParcelable(FlightHistoryDetailsFragment.ARG_FLIGHT_HISTORY)!!
    }
    lateinit var adapter: SelectPassengerAdapter
    private var actionCode = 0
    val viewModel: SelectPassengerVM by viewModels()
    private lateinit var passengerList: ArrayList<Traveller>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        passengerList =
            toTravellerList(travellersListToString(flightHistory.travellers!!)) as ArrayList<Traveller>
        viewModel.travellers = passengerList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView =
            FragmentFlightHistorySelectPassengerBinding.inflate(layoutInflater, container, false)
        bindingView.viewModel = viewModel

        // flightHistory = arguments?.getParcelable(FlightHistoryDetailsFragment.ARG_FLIGHT_HISTORY)!!
        actionCode = arguments?.getInt(FlightHistoryDetailsFragment.ACTION_CODE)!!

        adapter = SelectPassengerAdapter(passengerList, viewModel)
        bindingView.recyclerSelectPassenger.adapter = adapter

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.buttonNext.setOnClickListener {
            if (viewModel.isPassengerSelected()) {
                flightHistory.travellers = passengerList
                findNavController().navigate(
                    R.id.flight_history_change_ticket,
                    bundleOf(
                        ARGS_FLIGHT_HISTORY_DETAILS to flightHistory,
                        ACTION_CODE to actionCode
                    )
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.select_a_passenger),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        viewModel.updateTravellersLIst.observe(viewLifecycleOwner) {
            passengerList = it as ArrayList<Traveller>
            adapter.resetItems(passengerList)
        }

        return bindingView.root
    }

    companion object {
        const val ARGS_FLIGHT_HISTORY_DETAILS = "FLIGHT_HISTORY_DETAILS"
        const val ACTION_CODE = "ACTION_CODE"
    }
}