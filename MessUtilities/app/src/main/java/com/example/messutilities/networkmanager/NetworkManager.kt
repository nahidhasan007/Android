package com.example.messutilities.networkmanager

import com.example.messutilities.network.BuildRetrofit
import com.example.messutilities.network.FakeApi

object NetworkManager {
    val postApiService = BuildRetrofit.instance().create(FakeApi::class.java)
}