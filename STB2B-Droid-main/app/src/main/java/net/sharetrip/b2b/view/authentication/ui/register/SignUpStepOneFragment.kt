package net.sharetrip.b2b.view.authentication.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentSignUpStepOneBinding
import net.sharetrip.b2b.util.EventObserver

class SignUpStepOneFragment : Fragment() {
    private lateinit var viewModel: SignUpOneVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentSignUpStepOneBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(SignUpOneVM::class.java)
        bindingView.viewModel = viewModel

        bindingView.toolbar.setNavigationOnClickListener() {
            findNavController().navigateUp()
        }

        viewModel.showMessage.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        viewModel.moveToNextDestination.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(
                R.id.action_registration_initial_to_registration_final,
                bundleOf(ARG_AGENT_INFO to viewModel.getAgentInfo())
            )
        })

        return bindingView.root
    }
}
