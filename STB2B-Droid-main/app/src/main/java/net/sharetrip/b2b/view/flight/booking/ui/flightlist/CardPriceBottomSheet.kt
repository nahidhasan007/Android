package net.sharetrip.b2b.view.flight.booking.ui.flightlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.sharetrip.b2b.databinding.CardPriceBottomSheetBinding
import net.sharetrip.b2b.view.flight.booking.model.Flights
import net.sharetrip.b2b.view.flight.booking.ui.flightlist.FlightAdapter.Companion.AGENT_CLIENT_PRICE
import java.text.NumberFormat
import java.util.Locale

class CardPriceBottomSheet : BottomSheetDialogFragment(){
    private lateinit var priceBinding: CardPriceBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        priceBinding = CardPriceBottomSheetBinding.inflate(layoutInflater, container, false)
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)

//        val flights = requireArguments().getParcelableCompat(AGENT_CLIENT_PRICE, Flights::class.java)!!
        val flights : Flights = arguments?.getParcelable(AGENT_CLIENT_PRICE)!!
        priceBinding.apply {
            agentPrice.text ="BDT ${numberFormat.format(flights.finalPrice.toInt())}"
            clientPrice.text ="BDT ${numberFormat.format(flights.originPrice.toInt())}"

        }
        priceBinding.warningIcon.setOnClickListener {
            dismiss()
        }
        return priceBinding.root
    }
}