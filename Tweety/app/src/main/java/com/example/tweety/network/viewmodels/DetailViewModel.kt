package com.example.tweety.network.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tweety.model.TweetItem
import com.example.tweety.network.TweetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repo : TweetRepository,
    private val savedStateHandle: SavedStateHandle) : ViewModel(){
    val tweets : StateFlow<List<TweetItem>>
        get() = repo.tweets

    init {
        viewModelScope.launch {
            val category = savedStateHandle.get<String>("category") ?:"Food"
            repo.getTweets(category)
        }
    }
}