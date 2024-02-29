package net.sharetrip.b2b.view.authentication.ui.checkMail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentCheckMailBinding

class CheckMailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentCheckMailBinding.inflate(inflater, container, false)

        bindingView.toolbar.setOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_check_inbox_to_log_in_dest)
        }

        return bindingView.root
    }
}