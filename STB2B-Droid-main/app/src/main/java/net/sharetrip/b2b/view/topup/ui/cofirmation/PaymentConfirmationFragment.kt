package net.sharetrip.b2b.view.topup.ui.cofirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentPaymentConfirmationBinding
import net.sharetrip.b2b.view.topup.ui.paymentmethod.PaymentMethodFragment.Companion.ARG_IS_CONFIRMED

class PaymentConfirmationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView =
            FragmentPaymentConfirmationBinding.inflate(layoutInflater, container, false)
        bindingView.isConfirmed = requireArguments().getBoolean(ARG_IS_CONFIRMED)

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.btnHome.setOnClickListener {
            when (bindingView.btnHome.text) {
                getString(R.string.home) -> findNavController().navigate(R.id.confirmation_to_flight_search)
                getString(R.string.retry) -> findNavController().navigate(R.id.action_payment_confirmation_to_top_up_online)
            }
        }

        bindingView.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.confirmation_to_flight_search)
        }
        return bindingView.root
    }
}
