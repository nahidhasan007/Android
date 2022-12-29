package com.example.retrofitapi.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val id: Int,private val repo : ApiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetailViewModel::class.java))
            return DetailViewModel(id,repo) as T
        throw java.lang.IllegalArgumentException("Unknown Model Class!")
    }
}