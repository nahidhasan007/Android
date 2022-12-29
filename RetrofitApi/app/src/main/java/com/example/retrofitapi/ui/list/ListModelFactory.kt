package com.example.retrofitapi.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListModelFactory(private val repo: ListRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RetrofitModelView::class.java))
            return RetrofitModelView(repo) as T
        throw java.lang.IllegalArgumentException("Unknown Model Class!")
    }
}