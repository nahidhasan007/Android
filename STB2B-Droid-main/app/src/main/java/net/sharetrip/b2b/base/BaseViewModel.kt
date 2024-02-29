package net.sharetrip.b2b.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.sharetrip.b2b.util.Event

open class BaseViewModel : ViewModel() {
    val dataLoading = ObservableBoolean(false)

    val showMessage: LiveData<Event<String>>
        get() = _showMessage
    val _showMessage = MutableLiveData<Event<String>>()

    val navigateToDestination: LiveData<Event<Pair<String, Any>>>
        get() = _navigateToDestination
    private val _navigateToDestination = MutableLiveData<Event<Pair<String, Any>>>()

    protected fun navigateWithArgument(navigationKey: String, navigationArgument: Any) {
        _navigateToDestination.postValue(Event(Pair(navigationKey, navigationArgument)))
    }

    protected fun showMessage(msg: String?) {
        msg?.let {
            _showMessage.value = Event(msg)
        }
    }
}