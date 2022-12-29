package com.example.retrofitapi.ui.list

import com.example.retrofitapi.network.RetrofitApi

class ListRepository(private val api: RetrofitApi){
    suspend fun getPosts() = api.getPosts()
}