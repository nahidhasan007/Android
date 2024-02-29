package net.sharetrip.b2b.view.topup.ui.historydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.databinding.FragmentPaymentHistoryDetailsBinding
import net.sharetrip.b2b.view.topup.model.PaymentHistory

class HistoryDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView =
            FragmentPaymentHistoryDetailsBinding.inflate(layoutInflater, container, false)

        val paymentHistory: PaymentHistory = arguments?.getParcelable(ARG_HISTORY_DETAILS)!!
        bindingView.paymentHistory = paymentHistory

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return bindingView.root
    }

    companion object {
        const val ARG_HISTORY_DETAILS = "history_details"
    }
}