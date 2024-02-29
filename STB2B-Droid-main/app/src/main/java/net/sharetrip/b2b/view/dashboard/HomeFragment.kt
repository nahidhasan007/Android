package net.sharetrip.b2b.view.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentHomeBinding
import net.sharetrip.b2b.util.AppSharedPreference

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentHomeBinding.inflate(layoutInflater, container, false)
        bindingView.userName = AppSharedPreference.userName

        bindingView.flightsButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_flight_search)
        }

        return bindingView.root
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
