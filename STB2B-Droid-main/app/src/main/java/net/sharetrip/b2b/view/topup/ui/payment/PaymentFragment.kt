package net.sharetrip.b2b.view.topup.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentPaymentBinding
import net.sharetrip.b2b.network.PaymentEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.view.topup.model.PaymentHistory
import net.sharetrip.b2b.view.topup.ui.paymenthistory.PaymentHistoryAdapter

class PaymentFragment : Fragment() {
    private val adapter = PaymentHistoryAdapter()
    private val viewModel by lazy {
        val endPoint = ServiceGenerator.createService(PaymentEndPoint::class.java)
        ViewModelProvider(this, PaymentVMFactory(PaymentRepo(endPoint))).get(PaymentVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentPaymentBinding.inflate(inflater, container, false)
        bindingView.viewModel = viewModel
        bindingView.recyclerPaymentHistory.adapter = adapter

        bindingView.btnTopUpOnline.setOnClickListener {
            findNavController().navigate(R.id.action_payment_to_top_up_online)
        }

        bindingView.btnCreatePaymentRequest.setOnClickListener {
            findNavController().navigate(R.id.action_payment_to_payment_request)
        }

        bindingView.textViewAllPayments.setOnClickListener {
            findNavController().navigate(R.id.action_payment_dest_to_payment_history_dest)
        }

        viewModel.paymentHistoryList.observe(viewLifecycleOwner, EventObserver {
            adapter.addItem(it.data as ArrayList<PaymentHistory>)
        })

        viewModel.showMessage.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        return bindingView.root
    }

}
