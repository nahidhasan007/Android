package net.sharetrip.b2b.view.flight.booking.ui.search

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import net.sharetrip.b2b.BuildConfig
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentFlightSearchBinding
import net.sharetrip.b2b.util.*

class FlightSearchFragment : Fragment() {
    private lateinit var bindingView: FragmentFlightSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView = FragmentFlightSearchBinding.inflate(inflater, container, false)

        bindingView.pager.adapter = FlightSearchAdapter(this)
        hideKeyboard()

        TabLayoutMediator(bindingView.tabLayout, bindingView.pager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        checkLatestVersionAvailability()

        return bindingView.root
    }

    private fun getTabTitle(position: Int): String {
        return when (position) {
            ONE_WAY_INDEX -> getString(R.string.text_one_way)
            RETURN_INDEX -> getString(R.string.text_return)
            MULTI_CITY_INDEX -> getString(R.string.text_multi_city)
            else -> throw IndexOutOfBoundsException()
        }
    }

    private fun checkLatestVersionAvailability() {
        if (AppSharedPreference.versionCode < AppSharedPreference.latestCodeVersion) {
            if (!BuildConfig.DEBUG) {
                showDialog(AppSharedPreference.appVersionName)
            }
        }
    }

    private fun showDialog(version: String) {
        if (!requireActivity().isFinishing) {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_update)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val description = dialog.findViewById<AppCompatTextView>(R.id.textviewDescription)
            description.text =
                String.format(
                    requireContext().getString(R.string.app_update_text),
                    requireContext().getString(R.string.app_name),
                    version
                )

            val btnUpdate = dialog.findViewById<TextView>(R.id.buttonUpdate)
            btnUpdate.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(PLAY_STORE_URI)
                requireContext().startActivity(intent)
            }
            dialog.show()
        }
    }
}
