package net.sharetrip.b2b.view.flight.history.vrrvoid.passenger

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.sharetrip.b2b.databinding.ReissuePassengerDetailsBinding
import net.sharetrip.b2b.view.flight.history.refund.RefundVoidSharedViewModel

class VoidPassengerInfo : BottomSheetDialogFragment() {

    private lateinit var bindingView: ReissuePassengerDetailsBinding

    private val sharedViewModel: RefundVoidSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingView = ReissuePassengerDetailsBinding.inflate(layoutInflater, container, false)

        sharedViewModel.voidSelectedTraveller.observe(viewLifecycleOwner) { travellers ->
            Log.e("Travellers", travellers.toString())
            if (travellers.isNotEmpty()) {
                val adapter = PassengerAdapter(travellers)
                bindingView.passengerInfoRV.adapter = adapter
            }
        }


        bindingView.warningIcon.setOnClickListener {
            dismiss()
        }
        return bindingView.root
    }
}