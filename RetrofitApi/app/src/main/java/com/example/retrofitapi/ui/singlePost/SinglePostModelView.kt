package com.example.retrofitapi.ui.singlePost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitapi.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SinglePostModelView(id:Int, private val repo:SinglePostRepo) :ViewModel() {
    private var _singlePost = MutableLiveData<Post>()
    val singlePost : LiveData<Post> = _singlePost

    init {
        getSinglePost(id)
    }
    private fun getSinglePost(id:Int)
    {
        viewModelScope.launch(Dispatchers.IO)
        {
            val response = repo.getSinglePost(id)
            _singlePost.postValue(response)
        }
    }
}
