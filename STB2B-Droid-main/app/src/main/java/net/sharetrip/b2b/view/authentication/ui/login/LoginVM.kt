package net.sharetrip.b2b.view.authentication.ui.login

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.model.STException
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.view.authentication.model.Credential
import net.sharetrip.b2b.view.authentication.model.UserProfile

class LoginVM(private val repo: LoginRepo) : ViewModel() {
    val dataLoading = ObservableBoolean(false)
    val credential = ObservableField(Credential(String(), String()))

    val showMessage: LiveData<String>
        get() = _showMessage
    private val _showMessage = MutableLiveData<String>()

    val moveToDashBoard: LiveData<Boolean>
        get() = _moveToDashBoard
    private var _moveToDashBoard = MutableLiveData<Boolean>()

    fun clickOnLoginButton() {
        credential.get()?.let { credential ->
            if (!credential.isValid()) {
                _showMessage.value = MsgUtils.inValidInputMsg
            } else {
                viewModelScope.launch {
                    dataLoading.set(true)
                    val data = repo.fetchUserByEmailLogin(credential)
                    dataLoading.set(false)
                    loginResponseOperation(data)
                }
            }
        }
    }

    private fun loginResponseOperation(data: GenericResponse<RestResponse<UserProfile>>) {
        when (data) {
            is BaseResponse.Success ->
                saveUserProfile(data.body.response)

            is BaseResponse.ApiError ->
                _showMessage.value = data.errorBody.message

            is BaseResponse.NetworkError ->
                _showMessage.value = MsgUtils.networkErrorMsg

            is BaseResponse.UnknownError ->
                _showMessage.value = MsgUtils.unKnownErrorMsg
        }
    }

    private fun saveUserProfile(user: UserProfile) {
        viewModelScope.launch {
            try {
                dataLoading.set(true)
                repo.saveUserProfile(user)
                val userName = user.firstName + " " + user.lastName
                AppSharedPreference.accessToken = user.token
                AppSharedPreference.userName = userName
                FirebaseCrashlytics.getInstance().setUserId(userName)
                _moveToDashBoard.value = true
            } catch (exception: STException) {
                _showMessage.value = exception.message
            } finally {
                dataLoading.set(false)
            }
        }
    }

    fun onPause() {
        _moveToDashBoard.value = false
    }
}
