package net.sharetrip.b2b.view.authentication.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentLoginBinding
import net.sharetrip.b2b.localdb.LocalDataBase
import net.sharetrip.b2b.network.AuthEndPoint
import net.sharetrip.b2b.network.ServiceGenerator

class LoginFragment : Fragment() {
    private val viewModel: LoginVM by lazy {
        val endPoint = ServiceGenerator.createService(AuthEndPoint::class.java)
        val userDao = LocalDataBase.getDataBase(requireContext()).userProfileDao()
        LoginVMFactory(LoginRepo(endPoint, userDao)).create(LoginVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentLoginBinding.inflate(inflater, container, false)
        bindingView.viewModel = viewModel

        viewModel.showMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.moveToDashBoard.observe(viewLifecycleOwner) {
            if (it) findNavController().navigate(R.id.action_login_to_home)
        }

        bindingView.btnSignup.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_registration_initial)
        }

        bindingView.btnForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forget_password)
        }

        return bindingView.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }
}
