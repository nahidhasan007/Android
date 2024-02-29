package net.sharetrip.b2b.view.authentication.ui.register

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.view.authentication.model.AgentInformation
import net.sharetrip.b2b.view.authentication.model.UserProfile

class RegistrationVM(val repo: RegistrationRepo, agentInformation: AgentInformation) : ViewModel() {
    val dataLoading = ObservableBoolean(false)
    val isCheckedTermsAndCon = ObservableBoolean(false)
    val registrationCredential = ObservableField(AgentInformation())

    val showMessage: LiveData<String>
        get() = _showMessage
    private val _showMessage = MutableLiveData<String>()

    val moveToNextDestination: LiveData<Boolean>
        get() = _moveToNextDestination
    private var _moveToNextDestination = MutableLiveData<Boolean>()

    init {
        registrationCredential.set(agentInformation)
    }

    fun clickOnRegisterButton() {
        registrationCredential.get()?.let { credential ->
            if (!credential.isValid()) {
                _showMessage.value = MsgUtils.inValidInputMsg
            } else {
                viewModelScope.launch {
                    dataLoading.set(true)
                    val data = repo.signUpAgent(credential)
                    dataLoading.set(false)
                    registrationResponseOperation(data)
                }
            }
        }
    }

    fun onCheckedChanged(isCheck: Boolean) {
        isCheckedTermsAndCon.set(isCheck)
    }

    private fun registrationResponseOperation(data: GenericResponse<RestResponse<UserProfile>>) {
        when (data) {
            is BaseResponse.Success ->
                _moveToNextDestination.value = true

            is BaseResponse.ApiError ->
                _showMessage.value = data.errorBody.message

            is BaseResponse.NetworkError ->
                _showMessage.value = MsgUtils.networkErrorMsg

            is BaseResponse.UnknownError ->
                _showMessage.value = MsgUtils.unKnownErrorMsg
        }
    }
}
