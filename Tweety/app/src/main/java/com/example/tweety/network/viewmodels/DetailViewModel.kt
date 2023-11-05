package com.example.tweety.network.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweety.model.TweetItem
import com.example.tweety.network.TweetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repo : TweetRepository) : ViewModel(){
    val tweets : StateFlow<List<TweetItem>>
        get() = repo.tweets

    init {
        viewModelScope.launch {
            repo.getTweets("Food")
        }
    }
}