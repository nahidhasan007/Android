package net.sharetrip.b2b.view.authentication.ui.welcome

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ScreenSlidePagerAdapter(fa: Fragment, map: Map<Int, () -> Fragment>) :
    FragmentStateAdapter(fa) {
    private val tabFragmentsCreator: Map<Int, () -> Fragment> = map

    override fun getItemCount(): Int = tabFragmentsCreator.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreator[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}
