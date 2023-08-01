package com.example.messutilities.memberdetails

import androidx.lifecycle.viewModelScope
import com.example.messutilities.databse.MessDatabase
import com.example.messutilities.model.Members
import com.example.messutilities.shared.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemberDetailViewModel(private val database: MessDatabase) : BaseViewModel() {

    fun insertMembers(member : Members){
        viewModelScope.launch(Dispatchers.IO) {
            database.messDao().insert(member)
        }
    }

}