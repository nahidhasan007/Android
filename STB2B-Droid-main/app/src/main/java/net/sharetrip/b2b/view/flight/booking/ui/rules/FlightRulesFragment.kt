package net.sharetrip.b2b.view.flight.booking.ui.rules

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentRulesBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.CANCELLATION_POLICY_INDEX
import net.sharetrip.b2b.util.BAGGAGE_INDEX

class FlightRulesFragment : Fragment() {
    private val sharedVM: SharedVM by activityViewModels()
    private val viewModel: FlightRulesVM by lazy {
        val endPoint = ServiceGenerator.createService(FlightEndPoint::class.java)
        FlightRulesVMFactory(arguments, FlightRulesRepo(endPoint)).create(FlightRulesVM::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val bindingView = FragmentRulesBinding.inflate(inflater, container, false)

        bindingView.viewModel = viewModel
        val tabLayout = bindingView.tabLayout
        val viewPager = bindingView.pager

        viewPager.adapter = FlightRulesAdapter(this)
        viewPager.currentItem = arguments?.getInt(ARG_SELECTED_RULES)!!
        bindingView.toolbar.title = setTitle(viewPager.currentItem)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.flightRules.observe(viewLifecycleOwner) {
            sharedVM.setAirFareResponse(it)
        }

        viewModel.showMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bindingView.toolbar.title = setTitle(position)
                super.onPageSelected(position)
            }
        })

        return bindingView.root
    }

    private fun getTabTitle(position: Int): String {
        return setTitle(position)
    }

    private fun setTitle(position: Int): String {
        return when (position) {
            BAGGAGE_INDEX -> getString(R.string.baggage)
            CANCELLATION_POLICY_INDEX -> getString(R.string.cancellation_policy)
            else -> throw IndexOutOfBoundsException()
        }
    }

    companion object {
        const val ARG_SEARCH_ID = "search_id"
        const val ARG_SESSION_ID = "session_id"
        const val ARG_SEQUENCE_CODE = "sequence_code"
        const val ARG_SELECTED_RULES = "selected_rules"
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedVM.clearVM()
    }
}
