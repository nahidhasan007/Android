package com.example.messutilities.demopaging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.messutilities.network.FakeApi
import kotlinx.coroutines.flow.Flow


// the repository class is a layer that abstracts the data from the services(network service or local database) to the viewModel

class DemoPagingRepository(private val apiService: FakeApi) {

    fun getDemoAnyData(): Flow<PagingData<Any>> {
        val config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        )
        val source = DemoPagingSource(
            apiService
        )

        return Pager(
            config = config,
            pagingSourceFactory = { source }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}