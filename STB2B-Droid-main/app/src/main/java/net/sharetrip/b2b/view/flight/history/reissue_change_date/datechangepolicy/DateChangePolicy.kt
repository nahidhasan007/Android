package net.sharetrip.b2b.view.flight.history.reissue_change_date.datechangepolicy

import android.annotation.SuppressLint
import androidx.fragment.app.activityViewModels
import net.sharetrip.b2b.R
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.databinding.DateChangePolicyBinding
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ReissueChangeDateSharedViewModel
import net.sharetrip.b2b.widgets.BaseFragment

class DateChangePolicy : BaseFragment<DateChangePolicyBinding>() {
    override fun layoutId(): Int = R.layout.date_change_policy

    private val sharedVM: ReissueChangeDateSharedViewModel by activityViewModels()

    override fun getViewModel(): BaseViewModel? = null


    @SuppressLint("SetTextI18n")
    override fun initOnCreateView() {

        val fareRule = sharedVM.flightHistory?.airFareRules?.get(0)

        if (fareRule != null) {
            bindingView.originCode.text = fareRule.originCode + "-" + fareRule.destinationCode
            bindingView.originDestCity.text = fareRule.origin + "-" + fareRule.destination
            bindingView.policyText.text = fareRule.policy.rules[0].text
        }
    }
}