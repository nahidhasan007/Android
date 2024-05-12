package com.example.mvvmapp.network.remote

object DataManager {

    val remoteApiService = ServiceGenerator.createService(RemoteContentApi::class.java)
}