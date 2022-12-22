package com.example.crud.admin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminViewModel(application: Application) : AndroidViewModel(application){
    val readAdmin : LiveData<List<Superadmin>>
    private val repository:AdminRepository

    private val _isLoginSuccessful = MutableLiveData<Boolean>()
    val isLoginSuccessful : LiveData<Boolean>
    get() = _isLoginSuccessful


    init {
        val adminDao = AdminDatabase.getDatabase(application).AdminUserDao()
        repository = AdminRepository(adminDao)
        readAdmin = repository.adminData
    }
    fun addAdmin(admin:Superadmin)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.addAdmin(admin)
        }
    }
    fun adminLogin(userName:String,passWord:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
           var checkLogin = repository.adminLogin(userName,passWord)
            if (checkLogin.isEmpty())
            {
                _isLoginSuccessful.postValue(false)
            }
            else
            {
                _isLoginSuccessful.postValue(true)
            }
        }
    }

}