package net.sharetrip.b2b.view.flight.history.reissuepassengerdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.sharetrip.b2b.R
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.databinding.ReissuePassengerDetailsBinding
import net.sharetrip.b2b.view.flight.history.model.ModifyHistory
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.getParcelableCompat
import net.sharetrip.b2b.view.flight.history.reissueviewdetails.ReissueDetailsFragment.Companion.TRAVELLERS
import net.sharetrip.b2b.widgets.BaseFragment

class ReissuePassengerInfo : BottomSheetDialogFragment() {

    private lateinit var bindingView : ReissuePassengerDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingView = ReissuePassengerDetailsBinding.inflate(layoutInflater, container, false)

        val modifyHistory: ModifyHistory =
            requireArguments().getParcelableCompat(
                TRAVELLERS,
                ModifyHistory::class.java
            )!!
        val adapter = ReissuePassengerAdapter(
            modifyHistory.travellers
        )
        bindingView.passengerInfoRV.adapter = adapter
        bindingView.warningIcon.setOnClickListener {
            dismiss()
        }
        return bindingView.root
    }

//    override fun getViewModel(): BaseViewModel? = null
//
//    override fun initOnCreateView() {
//        val modifyHistory: ModifyHistory =
//            requireArguments().getParcelableCompat(
//                TRAVELLERS,
//                ModifyHistory::class.java
//            )!!
//        val adapter = ReissuePassengerAdapter(
//            modifyHistory.travellers
//        )
//        bindingView.passengerInfoRV.adapter = adapter
//    }
}