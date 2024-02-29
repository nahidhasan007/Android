package net.sharetrip.b2b.view.flight.history.historylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentFlightHistoryBinding
import net.sharetrip.b2b.util.FIRST_INDEX
import net.sharetrip.b2b.util.SECOND_INDEX
import net.sharetrip.b2b.util.THIRD_INDEX
import net.sharetrip.b2b.view.authentication.ui.welcome.ScreenSlidePagerAdapter

class FlightHistoryFragment : Fragment() {
    private val fragments = mapOf(
        FIRST_INDEX to { FlightHistoryListFragment(FIRST_INDEX) },
        SECOND_INDEX to { FlightHistoryListFragment(SECOND_INDEX) },
        THIRD_INDEX to { FlightHistoryListFragment(THIRD_INDEX) }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentFlightHistoryBinding.inflate(layoutInflater, container, false)

        val pagerAdapter = ScreenSlidePagerAdapter(this, fragments)
        bindingView.viewPager.adapter = pagerAdapter

        bindingView.buttonFilter.setOnClickListener {
            findNavController().navigate(R.id.action_flight_history_dest_to_filter)
        }

        bindingView.toolbar.setNavigationOnClickListener{
            findNavController().navigateUp()
        }

        TabLayoutMediator(bindingView.tabLayout, bindingView.viewPager) { tab, position ->
            tab.text = when (position) {
                FIRST_INDEX -> getString(R.string.all)
                SECOND_INDEX -> getString(R.string.issued_)
                THIRD_INDEX -> getString(R.string.booked)
                else -> throw IndexOutOfBoundsException()
            }
        }.attach()

        return bindingView.root
    }
}