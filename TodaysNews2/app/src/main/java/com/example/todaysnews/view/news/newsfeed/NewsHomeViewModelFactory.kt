package com.example.todaysnews.view.news.newsfeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todaysnews.network.NewsRepository

class NewsHomeViewModelFactory(private val newsApi : NewsRepository)  : ViewModelProvider.Factory{
    override fun<T: ViewModel> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(NewsHomeViewModel::class.java))
            return NewsHomeViewModel(newsApi) as T
        throw java.lang.IllegalArgumentException("Unknown Model Class")
    }
}