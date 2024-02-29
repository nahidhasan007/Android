package net.sharetrip.b2b.view.flight.booking.ui.rulesdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentRulesDetailsBinding
import net.sharetrip.b2b.util.CANCELLATION_POLICY_INDEX
import net.sharetrip.b2b.util.BAGGAGE_INDEX
import net.sharetrip.b2b.view.common.WebViewFragment
import net.sharetrip.b2b.view.flight.booking.model.AirFareResponse
import net.sharetrip.b2b.view.flight.booking.ui.rules.SharedVM
import net.sharetrip.b2b.widgets.ItemBaggageView

class RulesDetailsFragment(val position: Int) : Fragment() {
    private val sharedVM: SharedVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentRulesDetailsBinding.inflate(inflater, container, false)
        bindingView.textViewReadRules.setOnClickListener {
            findNavController().navigate(
                R.id.action_to_web_view,
                bundleOf(
                    WebViewFragment.ARGS_WEB_VIEW_DATA to getString(R.string.read_rules),
                    WebViewFragment.ARGS_TOOLBAR_TITLE to getString(R.string.how_to_read_rules)
                )
            )
        }

        sharedVM.airFareRules.observe(viewLifecycleOwner, {
            bindingView.position = position

            if (it is AirFareResponse)
                when (position) {
                    BAGGAGE_INDEX -> if (!it.baggages.isNullOrEmpty()) {
                        for (baggage in it.baggages) {
                            bindingView.layoutBaggageContainer.addView(ItemBaggageView(context).apply {
                                setBaggage(
                                    baggage
                                )
                            })
                        }
                    } else {
                        bindingView.rules = getString(R.string.baggage_rule_not_found)
                    }

                    CANCELLATION_POLICY_INDEX -> bindingView.rules =
                        if (it.getAirFareRulesDetails().isNotEmpty()) {
                            it.getAirFareRulesDetails()
                        } else {
                            getString(R.string.cancellation_policy_not_found)
                        }
                }
        })

        return bindingView.root
    }
}
