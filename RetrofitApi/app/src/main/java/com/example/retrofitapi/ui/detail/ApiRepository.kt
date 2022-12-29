package com.example.retrofitapi.ui.detail
import com.example.retrofitapi.network.RetrofitApi

class ApiRepository(private val api: RetrofitApi) {
  suspend fun getPostDetail(id:Int) = api.detailPost(id)
}