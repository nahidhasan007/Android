package net.sharetrip.b2b.view.authentication.ui.forget

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.EmptyResponse
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.util.isValidEmail

class ForgetPasswordVM(private val repo: ForgetPasswordRepo) : ViewModel() {
    val dataLoading = ObservableBoolean(false)
    val email = ObservableField(String())

    val showMessage: LiveData<String>
        get() = _showMessage
    private val _showMessage = MutableLiveData<String>()

    val moveToNext: LiveData<Event<Boolean>>
        get() = _moveToNext
    private var _moveToNext = MutableLiveData(Event(false))

    fun clickOnNextButton() {
        email.get()?.let { emailId ->
            if (emailId.isValidEmail()) {
                viewModelScope.launch {
                    dataLoading.set(true)
                    val data = repo.sendResetPasswordEmail(emailId)
                    operationOnResponse(data)
                    dataLoading.set(false)
                }
            } else {
                _showMessage.value = MsgUtils.invalidEmailMsg
            }
        }
    }

    private fun operationOnResponse(data: GenericResponse<RestResponse<EmptyResponse>>) {
        when (data) {
            is BaseResponse.Success ->
                _moveToNext.value = Event(true)

            is BaseResponse.ApiError ->
                _showMessage.value = data.errorBody.message

            is BaseResponse.NetworkError ->
                _showMessage.value = MsgUtils.networkErrorMsg

            is BaseResponse.UnknownError ->
                _showMessage.value = MsgUtils.unKnownErrorMsg
        }
    }
}
