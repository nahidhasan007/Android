package net.sharetrip.b2b.view.flight.history.changeticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentFlightHistoryChangeTicketBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.*
import net.sharetrip.b2b.view.common.WebViewFragment
import net.sharetrip.b2b.view.flight.booking.model.Flight
import net.sharetrip.b2b.view.flight.history.confirmation.ConfirmationFragment.Companion.ARG_FROM_WHERE
import net.sharetrip.b2b.view.flight.history.model.FlightHistory
import net.sharetrip.b2b.view.flight.history.passenger.SelectPassengerFragment
import net.sharetrip.b2b.view.topup.ui.paymentmethod.PaymentMethodFragment
import java.util.*

class ChangeTicketFragment : Fragment() {

    private var actionCode = 0
    var flightHistory: FlightHistory? = null
    var changeTicketAdapter = ChangeTicketAdapter()

    private val viewModel by lazy {
        val endPoint = ServiceGenerator.createService(FlightEndPoint::class.java)
        actionCode = arguments?.getInt(SelectPassengerFragment.ACTION_CODE)!!
        flightHistory =
            arguments?.getParcelable(SelectPassengerFragment.ARGS_FLIGHT_HISTORY_DETAILS)!!
        ChangeTicketVMFactory(
            ChangeTicketRepo(endPoint),
            flightHistory,
            actionCode
        ).create(ChangeTicketVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView =
            FragmentFlightHistoryChangeTicketBinding.inflate(layoutInflater, container, false)

        bindingView.viewModel = viewModel
        bindingView.actionCode = actionCode

        bindingView.checkBoxDataChangePolicy.text =
            fromHtml(getString(R.string.data_change_policy_terms_n_condition))

        bindingView.recyclerChangeTicket.adapter = changeTicketAdapter
        changeTicketAdapter.addItem(
            flightHistory?.flight as ArrayList<Flight>, actionCode == TICKET_ACTION_TEMPORARY_CANCEL
        )

        viewModel.moveToConfirmation.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(
                R.id.action_to_confirmation,
                bundleOf(PaymentMethodFragment.ARG_IS_CONFIRMED to it.first, ARG_FROM_WHERE to it.second)
            )
        })

        viewModel.showMessage.observe(viewLifecycleOwner, EventObserver {
            showToast(requireContext(), it)
        })

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.textViewDataChangePolicy.setOnClickListener {
            findNavController().navigate(
                R.id.action_to_web_view,
                bundleOf(
                    WebViewFragment.ARGS_WEB_VIEW_DATA to viewModel.flightPolicy,
                    WebViewFragment.ARGS_TOOLBAR_TITLE to getString(R.string.void_refund_cancellation_policy)
                )
            )
        }

        return bindingView.root
    }
}