package net.sharetrip.b2b.view.flight.booking.ui.rules

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import net.sharetrip.b2b.util.*
import net.sharetrip.b2b.view.flight.booking.ui.rulesdetails.RulesDetailsFragment

class FlightRulesAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentsCreator: Map<Int, () -> Fragment> = mapOf(
        BAGGAGE_INDEX to { RulesDetailsFragment(BAGGAGE_INDEX) },
        CANCELLATION_POLICY_INDEX to { RulesDetailsFragment(CANCELLATION_POLICY_INDEX) }
    )

    override fun getItemCount() = tabFragmentsCreator.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreator[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}
