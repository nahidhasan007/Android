package com.example.todaysnews.network

import com.example.todaysnews.model.Post
import retrofit2.Response
import retrofit2.http.GET

interface NewsApi {
     @GET("/posts")
     suspend fun getPosts() : List<Post>
}