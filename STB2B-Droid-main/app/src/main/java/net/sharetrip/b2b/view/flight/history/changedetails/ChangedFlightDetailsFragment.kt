package net.sharetrip.b2b.view.flight.history.changedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentChangedFlightDetailsBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.util.getTravellersWithID
import net.sharetrip.b2b.view.flight.history.changelist.ChangedFlightHistoryListFragment
import net.sharetrip.b2b.view.flight.history.passengerdetails.PassengerDetailsFragment

class ChangedFlightDetailsFragment : Fragment() {

    lateinit var uuid: String
    private val viewModel by lazy {
        val endPoint = ServiceGenerator.createService(FlightEndPoint::class.java)
        uuid = arguments?.getString(ChangedFlightHistoryListFragment.ARGUMENT_UUID_CODE)!!
        ChangedFlightDetailsVMFactory(ChangedFlightDetailsRepo(endPoint, uuid)).create(
            ChangedFlightDetailsVM::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView =
            FragmentChangedFlightDetailsBinding.inflate(layoutInflater, container, false)
        bindingView.viewModel = viewModel

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.includePassengerDetails.layoutArrow.setOnClickListener {
            viewModel.changedFlightDetails.get()?.let {
                findNavController().navigate(
                    R.id.action_changed_flight_details_to_passenger_details, bundleOf(
                        PassengerDetailsFragment.ARGS_PASSENGER_DETAILS to getTravellersWithID(
                            viewModel.changedFlightDetails.get()!!.passengers
                        ),
                        ARG_IS_DOMESTIC to viewModel.changedFlightDetails.get()!!.isDomestic
                    )
                )
            }
        }

        bindingView.includeRefundChargeDetails.layoutArrow.setOnClickListener {
            val bundle = Bundle()
            val refundDetails = viewModel.changedFlightDetails.get()!!.refundDetail
            refundDetails?.uuid = viewModel.changedFlightDetails.get()!!.uuid
            bundle.putParcelable(
                ARG_REFUNDABLE_DETAILS,
                viewModel.changedFlightDetails.get()!!
            )
            findNavController().navigate(
                R.id.action_changed_flight_details_to_refund_details,
                bundle
            )
        }

        bindingView.includeRequestActualBookingCode.rootLayout.setOnClickListener {
            viewModel.getActualBookingDetails()
        }

        viewModel.flightHistory.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(
                R.id.action_changed_flight_details_to_actual_booking_history,
                bundleOf(ARG_ACTUAL_HISTORY_DETAILS to it)
            )
        })

        return bindingView.root
    }

    companion object {
        const val ARG_IS_DOMESTIC = "ARG_IS_DOMESTIC"
        const val ARG_REFUNDABLE_DETAILS = "ARG_REFUNDABLE_DETAILS"
        // NB: DON'T CHANGE ARG_ACTUAL_HISTORY_DETAILS VALUE
        const val ARG_ACTUAL_HISTORY_DETAILS = "flight_history_details"
    }
}