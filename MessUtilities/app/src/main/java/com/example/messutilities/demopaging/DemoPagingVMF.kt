package com.example.messutilities.demopaging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.messutilities.network.FakeApi

class DemoPagingVMF(
    private val repository: DemoPagingRepository,
    private val apiService: FakeApi
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DemoPagingViewModel::class.java))
            return DemoPagingViewModel(repository, apiService) as T
        throw IllegalArgumentException("Unknown ViewModel Class")

    }
}