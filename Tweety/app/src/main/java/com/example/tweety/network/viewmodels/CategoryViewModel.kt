package com.example.tweety.network.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweety.model.Tweet
import com.example.tweety.model.TweetItem
import com.example.tweety.network.TweetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repo: TweetRepository) : ViewModel() {
    val categories : StateFlow<List<String>>
        get() = repo.categories

    val allTweets : StateFlow<Tweet>
        get() = repo.allTweets

    init {
        viewModelScope.launch {
            repo.getCategories()
        }
    }

    fun getAllTweet(){
        viewModelScope.launch {
            repo.getAllTweets()
        }
    }
}