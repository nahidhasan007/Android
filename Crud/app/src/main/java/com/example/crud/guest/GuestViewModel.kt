package com.example.crud.guest

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.crud.data.UserDatabase
import com.example.crud.data.UserRepository
import com.example.crud.guest.model.Guest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GuestViewModel(application: Application) : AndroidViewModel(application) {
    var readGuest : LiveData<List<Guest>>
    private val repository : UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application).UserDao()
        repository = UserRepository(userDao)
        readGuest = repository.readGuestDate
        Log.d("Guests", readGuest.toString())
    }

    fun addGuest(guest: Guest){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addGuest(guest)
        }
    }
}