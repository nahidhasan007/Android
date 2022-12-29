package com.example.retrofitapi.ui.singlePost

import com.example.retrofitapi.network.RetrofitApi

class SinglePostRepo(private val api:RetrofitApi) {

    suspend fun getSinglePost(id:Int) = api.singlePost(id)

}