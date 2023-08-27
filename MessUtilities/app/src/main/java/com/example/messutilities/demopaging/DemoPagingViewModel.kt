package com.example.messutilities.demopaging

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.messutilities.network.FakeApi
import com.example.messutilities.shared.BaseViewModel
import kotlinx.coroutines.flow.Flow

class DemoPagingViewModel (
    private val demoRepo: DemoPagingRepository,
    private val apiService : FakeApi
    ) : BaseViewModel(){
        var demoLiveData : Flow<PagingData<Any>> = demoRepo.getDemoAnyData().cachedIn(viewModelScope)

}