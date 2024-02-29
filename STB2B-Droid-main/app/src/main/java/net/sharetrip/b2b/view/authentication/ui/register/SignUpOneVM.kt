package net.sharetrip.b2b.view.authentication.ui.register

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.view.authentication.model.AgentInformation

class SignUpOneVM : ViewModel() {
    val registrationCredential = ObservableField(AgentInformation())

    val showMessage: LiveData<Event<String>>
        get() = _showMessage
    private val _showMessage = MutableLiveData<Event<String>>()

    val moveToNextDestination: LiveData<Event<Boolean>>
        get() = _moveToNextDestination
    private var _moveToNextDestination = MutableLiveData<Event<Boolean>>()

    fun clickOnNextButton() {
        registrationCredential.get()?.let { credential ->
            if (!credential.isValidInfo()) {
                _showMessage.value = Event(MsgUtils.inValidInputMsg)
            } else {
                _moveToNextDestination.value = Event(true)
            }
        }
    }

    fun getAgentInfo(): AgentInformation? {
        return registrationCredential.get()
    }
}
