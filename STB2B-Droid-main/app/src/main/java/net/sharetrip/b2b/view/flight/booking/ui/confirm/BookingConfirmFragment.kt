package net.sharetrip.b2b.view.flight.booking.ui.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentBookingConfirmBinding
import net.sharetrip.b2b.localdb.LocalDataBase
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.PaymentEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.view.flight.booking.model.ContactInfo
import net.sharetrip.b2b.view.flight.booking.ui.summary.BookingSummaryFragment.Companion.ARG_IS_CONFIRMED
import net.sharetrip.b2b.view.flight.booking.ui.summary.BookingSummaryRepo
import net.sharetrip.b2b.view.flight.booking.ui.summary.BookingSummaryVM
import net.sharetrip.b2b.view.flight.booking.ui.summary.BookingSummaryVMFactory
import net.sharetrip.b2b.view.flight.booking.ui.verification.CONTACT_VERIFICATION

class BookingConfirmFragment : Fragment() {
    private var isConfirmed: Boolean = false
    private val viewModel: BookingSummaryVM by lazy {
        val flightSearchDao = LocalDataBase.getDataBase(requireContext()).flightSearchDao()
        val passengerDao = LocalDataBase.getDataBase(requireContext()).passengerDao()
        val endPoint = ServiceGenerator.createService(FlightEndPoint::class.java)
        val paymentEndPoint = ServiceGenerator.createService(PaymentEndPoint::class.java)
        val contactInfo = arguments?.getParcelable(CONTACT_VERIFICATION) ?: ContactInfo()
        BookingSummaryVMFactory(
            BookingSummaryRepo(flightSearchDao, passengerDao, endPoint, paymentEndPoint),
            false, contactInfo
        ).create(
            BookingSummaryVM::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentBookingConfirmBinding.inflate(inflater, container, false)
        isConfirmed = requireArguments().getBoolean(ARG_IS_CONFIRMED)
        bindingView.viewModel = viewModel
        bindingView.isConfirmed = isConfirmed

        bindingView.btnHome.setOnClickListener {
            if (isConfirmed)
                findNavController().navigate(R.id.action_confirmation_to_home)
            else
                viewModel.checkBalance(true)
        }

        viewModel.showMessage.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        bindingView.btnCancel.setOnClickListener {
            findNavController().navigate(R.id.action_confirmation_to_home)
        }

        bindingView.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        return bindingView.root
    }
}
