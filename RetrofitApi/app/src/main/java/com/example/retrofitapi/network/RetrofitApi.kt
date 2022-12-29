package com.example.retrofitapi.network

import com.example.retrofitapi.model.Model
import com.example.retrofitapi.model.Post
import retrofit2.http.GET
import retrofit2.http.Path


interface RetrofitApi{

    @GET("/posts")
    suspend fun getPosts() : List<Model>

    @GET("/posts/{id}")
    suspend fun singlePost(@Path("id") Id:Int) : Post

    @GET("/posts/{id}/comments")
    suspend fun detailPost(@Path("id") id:Int) : List<Model>



}