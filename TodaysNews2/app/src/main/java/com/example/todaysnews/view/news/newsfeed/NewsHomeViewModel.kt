package com.example.todaysnews.view.news.newsfeed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todaysnews.model.Post
import com.example.todaysnews.network.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsHomeViewModel(private val newsRepo: NewsRepository) : ViewModel(){
    private var _posts = MutableLiveData<List<Post>>()
    val posts : LiveData<List<Post>> = _posts

    init {
        getPosts()
    }

    private fun getPosts(){
        viewModelScope.launch(Dispatchers.IO){
            val response = newsRepo.posts()
            if(response.isNotEmpty()){
                _posts.postValue(response)
            }
        }
    }
}