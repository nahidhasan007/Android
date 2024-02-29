package net.sharetrip.b2b.view.flight.history.reissue_change_date.flightlist

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.util.MsgUtils.networkErrorMsg
import net.sharetrip.b2b.util.MsgUtils.unKnownErrorMsg
import net.sharetrip.b2b.view.flight.booking.model.Airline
import net.sharetrip.b2b.view.flight.booking.model.FilterParams
import net.sharetrip.b2b.view.flight.history.reissue_change_date.flightlist.FlightListRepository.Companion.PAGE_SIZE
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.FlightX
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueQuotationFilterBody
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueQuotationRequestBody
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService
import java.io.IOException
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.*

class FlightPagingSource(
    var filterData: ReissueQuotationFilterBody,
    var quotationRequestBody: ReissueQuotationRequestBody,
    private val apiService: ReissueApiService,
    private val token: String
) : PagingSource<Int, FlightX>() {

    private val totalRecord = MutableLiveData<Int>()
    private val filterAirlinesList =
        MutableLiveData<List<Airline>>()
    private var filter: Filters? = null

    val searchId = MutableLiveData<String?>()
    val expiresAt = MutableLiveData<String?>()
    val sequenceCode = MutableLiveData<String?>()
    val sessionId = MutableLiveData<String>()
    val filterDeal = MutableLiveData<String>()
    val isInitialLoad = MutableLiveData(true)
    val isFirstPage = MutableLiveData(false)

    override fun getRefreshKey(state: PagingState<Int, FlightX>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FlightX> {
        var flightSearchResponse: List<FlightX>? = null
        val position: Int = params.key ?: FIRST_PAGE_ITEM
        if (position == FIRST_PAGE_ITEM) isFirstPage.postValue(true)
        else isFirstPage.postValue(false)

        val response =
            if (filterData.filters == null && position == FIRST_PAGE_ITEM) {
                apiService.reissueRequestQuotation(token, quotationRequestBody)
            } else {
                if (searchId.value != null) {
                    filterData.reissueSearchId = searchId.value!!
                    filterData.page = position

                }
                filterData.page = position
                if (filterData.filters == null) filterData.filters = FilterParams()
                apiService.reissueRequestQuotationFilter(token, filterData)
            }



        return when (response) {
            is BaseResponse.Success -> {
                val data = response.body.response
                if (data is ReissueFlightSearchResponse) {
                    if (data.flights.isNotEmpty()) {
                        flightSearchResponse = data.flights
                        if (position == 1) {
                            filterAirlinesList.postValue(data.filters?.airlines)
                            data.totalRecords?.let { totalRecord.postValue(it) }
                            if (data.automationSupported == true) {
                                searchId.postValue(data.reissueSearchId)
                                Log.e(TAG, "$TAG 1 searchId = ${searchId.value}")
                                isInitialLoad.value = false
                                filter = data.filters
                            } else {
                                Log.d("Expired AT", data.expiresAt!!)
                                if (data.status == "QUOTED") {
                                    searchId.value = data.reissueSearchId
                                    expiresAt.value = data.expiresAt
                                    Log.e(TAG, "$TAG 2 searchId = ${searchId.value}")
                                } else {
                                    if(data.manualSearchId != null) {
                                        searchId.value = data.manualSearchId
                                    }
                                    Log.e(TAG, "$TAG 3 searchId = ${searchId.value}")
                                }
                            }
                            Log.e(TAG, "$TAG reissueSearchId = ${data.reissueSearchId}")
                            Log.e(TAG, "$TAG manualSearchId = ${data.manualSearchId}")
                            Log.e(TAG, "$TAG searchId = ${searchId.value}")
                        }
                        createPage(flightSearchResponse, position)
                    } else {
                        if (position == FIRST_PAGE_ITEM) LoadResult.Error(Exception(noFlightFoundMsg))
                        else createPage(flightSearchResponse, position)
                    }
                } else {
                    data as ReissueQuotationFilterResponse
                    if (data.flights.isNotEmpty()) {
                        flightSearchResponse = data.flights
                        createPage(flightSearchResponse, position)
                    } else {
                        createPage(flightSearchResponse, position)
                    }
                }
            }
            is BaseResponse.ApiError -> LoadResult.Error(Exception(response.errorBody.message))

            is BaseResponse.NetworkError -> {
                val data = response.error
                data.printStackTrace()
                LoadResult.Error(IOException(networkErrorMsg))
            }

            is BaseResponse.UnknownError -> {
                val data = response.error
                data.printStackTrace()
                LoadResult.Error(Exception(unKnownErrorMsg))
            }
        }
    }

    private fun createPage(
        flightSearchResponse: List<FlightX>?,
        position: Int,
    ) = LoadResult.Page(
        data = flightSearchResponse.orEmpty(),
        prevKey = if (position == FIRST_PAGE_ITEM) null else position - 1,
        nextKey = if (flightSearchResponse.isNullOrEmpty() || flightSearchResponse.size < PAGE_SIZE) null else position + 1
    )

    fun filterAirlinesList(): MutableLiveData<List<Airline>> {
        return filterAirlinesList
    }

    fun flightFilter(): Filters? {
        return filter
    }

    fun totalRecord(): MutableLiveData<Int> {
        return totalRecord
    }

    companion object {
        private const val FIRST_PAGE_ITEM = 1
        const val noFlightFoundMsg =
            "We've searched more than 100 airlines that we sell, and couldn't find any flights on these dates. Please try Changing your search details."
        const val TAG = "FlightPagingSource"
    }
}

