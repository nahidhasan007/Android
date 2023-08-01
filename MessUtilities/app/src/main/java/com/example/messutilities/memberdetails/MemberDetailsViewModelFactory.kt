package com.example.messutilities.memberdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.messutilities.databse.MessDatabase

class MemberDetailsViewModelFactory(
    private val database: MessDatabase
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MemberDetailViewModel::class.java))
            return MemberDetailViewModel(database) as T
        throw IllegalAccessException("ViewModel is not correct!")
    }
}