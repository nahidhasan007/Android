package net.sharetrip.b2b.view.flight.history.confirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.ConfirmationFragmentBinding
import net.sharetrip.b2b.view.topup.ui.paymentmethod.PaymentMethodFragment

class ConfirmationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView =
            ConfirmationFragmentBinding.inflate(layoutInflater, container, false)
        bindingView.fromWhere = requireArguments().getString(ARG_FROM_WHERE)
        bindingView.isConfirmed =
            requireArguments().getBoolean(PaymentMethodFragment.ARG_IS_CONFIRMED)

        bindingView.btnHome.setOnClickListener {
            val text: String = bindingView.btnHome.text as String
            when {
                text.equals(
                    getString(R.string.home),
                    ignoreCase = true
                ) -> findNavController().navigate(R.id.confirmation_to_flight_search)
                text.equals(
                    getString(R.string.retry),
                    ignoreCase = true
                ) -> findNavController().navigateUp()
            }
        }

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.confirmation_to_flight_search)
        }
        return bindingView.root
    }

    companion object {
        const val ARG_FROM_WHERE = "from_where"
    }
}