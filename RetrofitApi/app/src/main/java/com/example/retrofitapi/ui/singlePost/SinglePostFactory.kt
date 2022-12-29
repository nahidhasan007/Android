package com.example.retrofitapi.ui.singlePost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SinglePostFactory(private val id:Int, private val repo:SinglePostRepo) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SinglePostModelView::class.java))
            return SinglePostModelView(id,repo) as T
        throw java.lang.IllegalArgumentException("Model class invalid!")
    }
}