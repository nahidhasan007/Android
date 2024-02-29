package net.sharetrip.b2b.view.flight.history.reissue_change_date.flightlist


import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.PagingConfig

import kotlinx.coroutines.flow.Flow
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.*
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlightSearch
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueQuotationRequestBody
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService

class FlightListRepository(
    val flightSearch: ReissueFlightSearch,
    val apiService: ReissueApiService,
    val token: String
) {
    val flightPagingSource = MutableLiveData<FlightPagingSource>()

    fun getFlightListFromRepository(
        quotationRequestBody: ReissueQuotationRequestBody,
        filter: ReissueQuotationFilterBody
    ): Flow<PagingData<FlightX>> {
        val config = PagingConfig(
            pageSize = PAGE_SIZE,
            initialLoadSize = PAGE_SIZE,
            prefetchDistance = PAGE_SIZE,
            enablePlaceholders = false
        )
        val source =
            FlightPagingSource(
                filter,
                quotationRequestBody,
                apiService,
                token
            )

        flightPagingSource.postValue(source)

        return Pager(
            config = config,
            pagingSourceFactory = { source }
        ).flow
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}
