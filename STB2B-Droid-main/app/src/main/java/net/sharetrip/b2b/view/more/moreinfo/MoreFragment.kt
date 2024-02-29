package net.sharetrip.b2b.view.more.moreinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentMoreBinding
import net.sharetrip.b2b.localdb.LocalDataBase
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.util.ShareTripB2B
import net.sharetrip.b2b.util.analytics.B2BEvent

class MoreFragment : Fragment() {
    private val viewModel by lazy {
        val userDao = LocalDataBase.getDataBase(requireContext()).userProfileDao()
        ViewModelProvider(this, MoreVMFactory(MoreRepo(userDao))).get(
            MoreVM::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentMoreBinding.inflate(layoutInflater, container, false)
        bindingView.viewModel = viewModel

        viewModel.userProfile.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(
                R.id.action_more_to_user_profile,
                bundleOf(ARG_USER_PROFILE to it)
            )
        })

        viewModel.moveToLogin.observe(viewLifecycleOwner, {
            ShareTripB2B.getB2BAnalyticsManager(requireContext())
                .trackEvent(B2BEvent.FlightEvent.CLICK_LOGOUT)
            findNavController().navigate(R.id.action_more_to_login_dest)
        })


        viewModel.moveToQuickPassenger.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(R.id.action_more_to_quick_passenger)
        })

        return bindingView.root
    }

    companion object {
        const val ARG_USER_PROFILE = "user_profile"
    }
}