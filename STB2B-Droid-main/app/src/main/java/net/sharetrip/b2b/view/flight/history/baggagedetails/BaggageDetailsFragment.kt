package net.sharetrip.b2b.view.flight.history.baggagedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentBaggageDetailsBinding
import net.sharetrip.b2b.view.flight.booking.model.Baggage
import net.sharetrip.b2b.widgets.ItemBaggageView

class BaggageDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentBaggageDetailsBinding.inflate(layoutInflater, container, false)
        val baggageInfo: List<Baggage> = arguments?.getParcelableArrayList(ARGS_BAGGAGE_INFO)!!

        if (!baggageInfo.isNullOrEmpty()) {
            for (baggage in baggageInfo) {
                bindingView.layoutBaggageContainer.addView(ItemBaggageView(context).apply {
                    setBaggage(
                        baggage
                    )
                })
            }
        } else {
            bindingView.tvNoBaggageInfo.text = getString(R.string.no_baggage_info)
        }

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return bindingView.root
    }

    companion object {
        const val ARGS_BAGGAGE_INFO = "baggage_info"
    }
}