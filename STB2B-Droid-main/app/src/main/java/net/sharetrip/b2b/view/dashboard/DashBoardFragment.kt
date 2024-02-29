package net.sharetrip.b2b.view.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentDashBoardBinding

class DashBoardFragment : Fragment() {
    private lateinit var bindingView: FragmentDashBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView = FragmentDashBoardBinding.inflate(inflater, container, false)

        bindingView.bottomNavigationView.setOnNavigationItemSelectedListener(
            navigationItemSelectedListener
        )

        return bindingView.root
    }

    override fun onResume() {
        bindingView.bottomNavigationView.selectedItemId = R.id.tab_home
        super.onResume()
    }

    private val navigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {

                when (item.itemId) {
                    R.id.tab_home -> {
                        // openFragment(HomeFragment.newInstance())
                        return true
                    }

                    R.id.tab_booking -> {
                        // openFragment(UpcomingFragment.newInstance())
                        return true
                    }

                    R.id.tab_transaction -> {
                        // openFragment(UpcomingFragment.newInstance())
                        return true
                    }

                    R.id.tab_payment -> {
                        //   openFragment(PaymentFragment.newInstance())
                        return true
                    }

                    R.id.tab_more -> {
                        //  openFragment(UpcomingFragment.newInstance())
                        return true
                    }
                }

                return true
            }
        }

    fun openFragment(fragment: Fragment) {
        val transaction: FragmentTransaction =
            requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
