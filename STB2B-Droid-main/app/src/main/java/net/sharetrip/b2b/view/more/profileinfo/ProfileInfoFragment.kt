package net.sharetrip.b2b.view.more.profileinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.databinding.FragmentProfileInfoBinding
import net.sharetrip.b2b.view.authentication.model.UserProfile
import net.sharetrip.b2b.view.more.moreinfo.MoreFragment.Companion.ARG_USER_PROFILE

class ProfileInfoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentProfileInfoBinding.inflate(layoutInflater, container, false)

        val userProfile: UserProfile = arguments?.getParcelable(ARG_USER_PROFILE)!!
        bindingView.userProfile = userProfile

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        return bindingView.root
    }
}