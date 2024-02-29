package net.sharetrip.b2b.view.authentication.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.sharetrip.b2b.view.authentication.model.AgentInformation

class RegistrationVMFactory(
    val repo: RegistrationRepo,
    private val agentInformation: AgentInformation
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationVM::class.java))
            return RegistrationVM(repo, agentInformation) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
