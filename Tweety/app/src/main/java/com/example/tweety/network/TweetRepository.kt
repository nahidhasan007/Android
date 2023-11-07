package com.example.tweety.network

import com.example.tweety.api.TweetApi
import com.example.tweety.model.Tweet
import com.example.tweety.model.TweetItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class TweetRepository @Inject constructor(private val tweetApi : TweetApi){

    private val _categories  = MutableStateFlow<List<String>>(emptyList())
    val categories : StateFlow<List<String>>
        get() = _categories

    private val _tweets  = MutableStateFlow<List<TweetItem>>(emptyList())
    val tweets : StateFlow<List<TweetItem>>
        get() = _tweets

    private val _allTweets  = MutableStateFlow(Tweet(listOf()))
    val allTweets : StateFlow<Tweet>
        get() = _allTweets

    suspend fun getCategories(){
        val response = tweetApi.getCategories()
        if(response.isSuccessful && response.body()!=null){
            _categories.emit(response.body()!!)
        }
    }

    suspend fun getTweets(category: String){
        val response = tweetApi.getTweets("tweets[?(@.category==\"$category\")]")
        if(response.isSuccessful && response.body()!=null){
            _tweets.emit(response.body()!!)
        }
    }

    suspend fun getAllTweets(){
        val response = tweetApi.getAllTweet()
        _allTweets.emit(response)
    }
}