package com.example.messutilities.network

import com.example.messutilities.model.Post
import retrofit2.http.GET
import retrofit2.http.Query

interface FakeApi {
    // to do fakeApi Response
    @GET("api/")
    suspend fun getPics(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int)
    : List<Any>
}