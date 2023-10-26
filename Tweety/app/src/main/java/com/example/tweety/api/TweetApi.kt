package com.example.tweety.api

import com.example.tweety.model.TweetItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface TweetApi {
    @GET("/v3/b/653a2bea12a5d3765990b952?meta=false")
    suspend fun getTweets(
        @Header("X-JSON-Path") category: String)
        : Response<List<TweetItem>>

    @GET("/v3/b/653a2bea12a5d3765990b952?meta=false")
    @Headers("X-JSON-Path:tweets..category")
    suspend fun getCategories() : Response<List<String>>
}