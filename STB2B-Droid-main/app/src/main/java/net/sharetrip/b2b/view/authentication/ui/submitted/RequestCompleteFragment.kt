package net.sharetrip.b2b.view.authentication.ui.submitted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R

import net.sharetrip.b2b.databinding.FragmentRequestSubmittedBinding

class RequestCompleteFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentRequestSubmittedBinding.inflate(inflater, container, false)

        bindingView.btnLogin.setOnClickListener {
            //findNavController().navigate(R.id.action_request_submitted_to_login)
        }

        return bindingView.root
    }
}