package com.example.todaysnews.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BaseRetrofitBuilder {
    private const val baseUrl = "https://jsonplaceholder.typicode.com"
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}