package com.example.messutilities.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BuildRetrofit {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    fun instance() : Retrofit{
        return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }
}