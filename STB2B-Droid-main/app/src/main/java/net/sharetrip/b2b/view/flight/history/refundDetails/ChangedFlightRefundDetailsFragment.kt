package net.sharetrip.b2b.view.flight.history.refundDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentChangedFlightRefundDetailsBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.showToast
import net.sharetrip.b2b.view.flight.history.changedetails.ChangedFlightDetailsFragment
import net.sharetrip.b2b.view.flight.history.model.ChangeTicketDetails
import net.sharetrip.b2b.view.flight.history.model.RefundDetail
import net.sharetrip.b2b.view.flight.history.model.RefundRequest

class ChangedFlightRefundDetailsFragment : Fragment() {

    lateinit var refundDetails: RefundDetail
    lateinit var changeTicketDetails: ChangeTicketDetails

    private val viewModel by lazy {
        val endPoint = ServiceGenerator.createService(FlightEndPoint::class.java)
        ChangedFlightRefundDetailsVMFactory(ChangedFlightRefundDetailsRepo(endPoint)).create(
            ChangedFlightRefundDetailsVM::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView =
            FragmentChangedFlightRefundDetailsBinding.inflate(layoutInflater, container, false)
        bindingView.viewModel = viewModel
        changeTicketDetails =
            arguments?.getParcelable(ChangedFlightDetailsFragment.ARG_REFUNDABLE_DETAILS)!!
        refundDetails = changeTicketDetails.refundDetail!!
        bindingView.refundDetails = refundDetails

        changeTicketDetails.expiredAt?.let {
            changeTicketDetails.requestStatus?.let { it1 ->
                viewModel.checkExpireTimeVisibility(it,
                    it1
                )
            }
            bindingView.expireTime = it
        }

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.buttonAcceptRefundCharge.setOnClickListener {
            refundDetails.uuid?.let { it1 -> RefundRequest(it1, true) }?.let { it2 ->
                viewModel.updateRefundRequest(
                    it2
                )
            }
        }

        bindingView.buttonDeclineAndCancelRequest.setOnClickListener {
            refundDetails.uuid?.let { it1 -> RefundRequest(it1, false) }?.let { it2 ->
                viewModel.updateRefundRequest(
                    it2
                )
            }
        }

        viewModel.reqResult.observe(viewLifecycleOwner, {
            if (it == "SUCCESS") {
                showToast(requireContext(), "Your request is successful")
                findNavController().navigate(R.id.action_changed_flight_refunds_to_changed_ticket_list)
            } else {
                showToast(requireContext(), "Your request is failed!")
                findNavController().navigate(R.id.action_changed_flight_refunds_to_changed_ticket_list)
            }
        })

        return bindingView.root
    }
}