package net.sharetrip.b2b.view.authentication.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentLaunchingBinding
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.util.FIRST_INDEX
import net.sharetrip.b2b.util.SECOND_INDEX
import net.sharetrip.b2b.util.THIRD_INDEX

class LaunchingFragment : Fragment() {
    private val fragments = mapOf(
        FIRST_INDEX to { WelcomeFragment.newInstance(FIRST_INDEX) },
        SECOND_INDEX to { WelcomeFragment.newInstance(SECOND_INDEX) },
        THIRD_INDEX to { WelcomeFragment.newInstance(THIRD_INDEX) }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentLaunchingBinding.inflate(inflater, container, false)

        val pagerAdapter = ScreenSlidePagerAdapter(this, fragments)
        bindingView.viewPager.adapter = pagerAdapter

        if (AppSharedPreference.accessToken.isNotEmpty()) {
            findNavController().navigate(R.id.action_welcome_to_dash_board_dest)
        }

        bindingView.btnGetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_login)
        }

        bindingView.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setUpIndicator(bindingView.linearLayoutIndicator, position)
                super.onPageSelected(position)
            }
        })
        return bindingView.root
    }

    private fun setUpIndicator(linearLayout: LinearLayout, selectedPosition: Int) {
        linearLayout.removeAllViews()

        linearLayout.post {
            linearLayout.invalidate()
            linearLayout.requestLayout()
        }

        for (i in 0 until 3) {
            val dot = ImageView(requireContext())
            val drawableId: Int =
                if (i == selectedPosition) R.drawable.active_dot else R.drawable.non_active_indicator
            dot.setImageResource(drawableId)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 8, 8, 8)
            linearLayout.addView(dot, params)
        }
    }
}
