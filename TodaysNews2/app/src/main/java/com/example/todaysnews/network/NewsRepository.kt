package com.example.todaysnews.network

class NewsRepository(private val apiService : NewsApi) {
    suspend fun posts() = apiService.getPosts()
}