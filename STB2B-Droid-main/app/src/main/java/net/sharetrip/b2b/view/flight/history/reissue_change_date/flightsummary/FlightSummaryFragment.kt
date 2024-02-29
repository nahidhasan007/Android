package net.sharetrip.b2b.view.flight.history.reissue_change_date.flightsummary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import net.sharetrip.b2b.databinding.FragmentOfReissuePaymentSummaryBinding
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ReissueChangeDateSharedViewModel

class FlightSummaryFragment: Fragment() {
    companion object {
        const val TAG = "FlightSummaryFragment"
    }

    private var _bindingView: FragmentOfReissuePaymentSummaryBinding? = null
    private val bindingView: FragmentOfReissuePaymentSummaryBinding get() = _bindingView!!

    private val sharedViewModel: ReissueChangeDateSharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this._bindingView = FragmentOfReissuePaymentSummaryBinding.inflate(inflater, container, false)
        this.bindingView.root.setOnClickListener {  }
        return this.bindingView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingView = null
    }
}