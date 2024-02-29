package net.sharetrip.b2b.view.authentication.ui.forget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentForgetPasswordBinding
import net.sharetrip.b2b.network.AuthEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.EventObserver

class ForgetPassWordFragment : Fragment() {
    private val viewModel: ForgetPasswordVM by lazy {
        val endPoint = ServiceGenerator.createService(AuthEndPoint::class.java)
        ForgetPasswordVMFactory(ForgetPasswordRepo(endPoint)).create(ForgetPasswordVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        bindingView.viewModel = viewModel

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.showMessage.observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        viewModel.moveToNext.observe(viewLifecycleOwner,EventObserver {
            if (it)
                findNavController().navigate(R.id.action_forget_password_to_check_inbox)
        })

        return bindingView.root
    }
}
