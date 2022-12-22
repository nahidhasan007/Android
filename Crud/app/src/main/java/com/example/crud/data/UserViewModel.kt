package com.example.crud.data

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    val readData: LiveData<List<User>>
    private val repository:UserRepository
    init {
        val userDao = UserDatabase.getDatabase(application).UserDao()
        repository = UserRepository(userDao)
        readData = repository.realAllData
        Log.i(TAG, readData.value?.size.toString())
    }
    fun addUser(user: User)
    {
        viewModelScope.launch(Dispatchers.IO){
            repository.addUser(user)
        }
    }
    fun userUpdate(user: User)
    {
        viewModelScope.launch(Dispatchers.IO){
            repository.userUpdate(user)
        }
    }
    fun userDelete(user: User)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.userDelete(user)
        }
    }

}