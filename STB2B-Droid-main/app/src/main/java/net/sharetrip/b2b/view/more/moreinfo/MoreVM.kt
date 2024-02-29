package net.sharetrip.b2b.view.more.moreinfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.view.authentication.model.UserProfile

class MoreVM(private val repo: MoreRepo) : ViewModel() {
    val userProfile = MutableLiveData<Event<UserProfile>>()
    val moveToQuickPassenger = MutableLiveData<Event<Boolean>>()
    val moveToLogin = MutableLiveData<Boolean>()

    fun logOut() {
        viewModelScope.launch {
            repo.deleteUser()
            moveToLogin.value = true
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            userProfile.value = Event(repo.getUserProfile())
        }
    }

    fun onClickedQuickPassenger() {
        moveToQuickPassenger.value = Event(true)
    }
}