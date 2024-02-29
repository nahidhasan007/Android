package net.sharetrip.b2b.view.authentication.ui.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentSignUpStepTwoBinding
import net.sharetrip.b2b.network.AuthEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.DateUtils.getCalender
import net.sharetrip.b2b.util.formatToTwoDigit
import net.sharetrip.b2b.view.authentication.model.AgentInformation

class SignUpStepTwoFragment : Fragment() {
    private val viewModel: RegistrationVM by lazy {
        val endPoint = ServiceGenerator.createService(AuthEndPoint::class.java)
        val agentInformation = arguments?.getParcelable(ARG_AGENT_INFO) ?: AgentInformation()

        RegistrationVMFactory(
            RegistrationRepo(endPoint), agentInformation
        ).create(RegistrationVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentSignUpStepTwoBinding.inflate(inflater, container, false)
        bindingView.viewModel = viewModel

        bindingView.tvTc.movementMethod = LinkMovementMethod.getInstance()

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.textFieldDate.setOnClickListener {
            val datePickerDialog =
                DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
                    val month = formatToTwoDigit(monthOfYear + 1)
                    val day = formatToTwoDigit(dayOfMonth)

                    val date = "$year-$month-$day"
                    viewModel.registrationCredential
                    bindingView.textFieldDate.setText(date)
                }, getCalender().year, getCalender().month, getCalender().day)

            datePickerDialog.show()
        }

        viewModel.showMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.moveToNextDestination.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_registration_final_to_request_submitted)
        }

        return bindingView.root
    }
}

const val ARG_AGENT_INFO = "agent_info"
