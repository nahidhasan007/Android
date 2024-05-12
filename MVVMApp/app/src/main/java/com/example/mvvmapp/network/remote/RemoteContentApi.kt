package com.example.mvvmapp.network.remote

import com.example.mvvmapp.base.GenericResponse
import com.example.mvvmapp.base.RestResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface RemoteContentApi {
    @GET
    suspend fun getVideoContent(
        @Url url: String = ServiceGenerator.base_url
    ) : GenericResponse<RestResponse<Any>>
}