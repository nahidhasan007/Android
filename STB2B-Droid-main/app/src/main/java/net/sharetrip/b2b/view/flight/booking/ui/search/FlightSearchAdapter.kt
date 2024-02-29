package net.sharetrip.b2b.view.flight.booking.ui.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import net.sharetrip.b2b.util.MULTI_CITY_INDEX
import net.sharetrip.b2b.util.ONE_WAY_INDEX
import net.sharetrip.b2b.util.RETURN_INDEX
import net.sharetrip.b2b.view.flight.booking.ui.multicity.MultiCityFragment
import net.sharetrip.b2b.view.flight.booking.ui.oneway.OneWayFragment
import net.sharetrip.b2b.view.flight.booking.ui.regreso.ReturnFragment

class FlightSearchAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentsCreator: Map<Int, () -> Fragment> = mapOf(
        ONE_WAY_INDEX to { OneWayFragment() },
        RETURN_INDEX to { ReturnFragment() },
        MULTI_CITY_INDEX to { MultiCityFragment() }
    )

    override fun getItemCount(): Int = tabFragmentsCreator.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreator[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}
