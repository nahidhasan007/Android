package net.sharetrip.b2b.view.flight.booking.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentBookingSummaryBinding
import net.sharetrip.b2b.localdb.LocalDataBase
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.PaymentEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.view.flight.booking.model.ContactInfo
import net.sharetrip.b2b.view.flight.booking.model.Flights
import net.sharetrip.b2b.view.flight.booking.ui.flightlist.ItemFlightView
import net.sharetrip.b2b.view.flight.booking.ui.verification.CONTACT_VERIFICATION
import net.sharetrip.b2b.view.flight.history.model.PriceDetails
import net.sharetrip.b2b.widgets.ItemCommonPriceBreakDownView

class BookingSummaryFragment : Fragment() {
    lateinit var bindingView: FragmentBookingSummaryBinding
    private var isConfirmed = false
    lateinit var contactInfo: ContactInfo
    lateinit var priceDetails: PriceDetails

    private val viewModel: BookingSummaryVM by lazy {
        val flightSearchDao = LocalDataBase.getDataBase(requireContext()).flightSearchDao()
        val passengerDao = LocalDataBase.getDataBase(requireContext()).passengerDao()
        val endPoint = ServiceGenerator.createService(FlightEndPoint::class.java)
        val paymentEndPoint = ServiceGenerator.createService(PaymentEndPoint::class.java)

        contactInfo = arguments?.getParcelable(CONTACT_VERIFICATION) ?: ContactInfo()
        BookingSummaryVMFactory(
            BookingSummaryRepo(flightSearchDao, passengerDao, endPoint, paymentEndPoint),
            true,
            contactInfo
        ).create(
            BookingSummaryVM::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView = FragmentBookingSummaryBinding.inflate(inflater, container, false)
        bindingView.includePriceBottomSheet.buttonContinue.isEnabled = false
        bindingView.includePriceBottomSheet.buttonContinue.text = getString(R.string.book_tickets)

        viewModel.showMessage.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        viewModel.moveToConfirmation.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(
                R.id.action_booking_summary_to_confirmation, bundleOf(
                    ARG_IS_CONFIRMED to isConfirmed, CONTACT_VERIFICATION to contactInfo
                )
            )
        })

        viewModel.isConfirmed.observe(viewLifecycleOwner, EventObserver {
            isConfirmed = it
        })

        bindingView.includePriceBottomSheet.buttonContinue.setOnClickListener {
            viewModel.checkBalance(true)
        }

        bindingView.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }

        viewModel.bookingButtonStatus.observe(viewLifecycleOwner) {
            bindingView.includePriceBottomSheet.buttonContinue.isEnabled = it.second
            bindingView.includePriceBottomSheet.buttonContinue.text = it.first
        }

        viewModel.flightSearch.observe(viewLifecycleOwner) {
            bindingView.viewModel = viewModel

            if (it.flights != null) {
                if (!it.flights!!.flight.isNullOrEmpty()) {
                    for (flight in it.flights!!.flight!!) {
                        val itemFlightView =
                            ItemFlightView(requireContext()).apply { setItemFlight(flight) }
                        bindingView.listFlight.addView(itemFlightView)
                    }
                }

                commonPriceBreakdown(it.flights!!)
            }
        }
        return bindingView.root
    }

    private fun commonPriceBreakdown(flights: Flights) {
        priceDetails = PriceDetails(
            null, flights.currency, flights.price, null,
            flights.discountAmount, flights.advanceIncomeTax, flights.originPrice, null,
            flights.originPrice, flights.finalPrice
        )
        bindingView.priceDetails = priceDetails
        val view = ItemCommonPriceBreakDownView(requireContext())
        view.setPriceBreakDown(priceDetails)
        bindingView.includePriceBottomSheet.layoutCommonPriceContainer.addView(view)
    }

    companion object {
        const val ARG_IS_CONFIRMED = "isConfirmed"
    }
}
