package net.sharetrip.b2b.view.flight.booking.ui.flightlist

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.STException
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericRestResponse
import net.sharetrip.b2b.network.NetworkState
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.util.MsgUtils.available_flights
import net.sharetrip.b2b.view.flight.booking.model.*

class FlightListVM(private val repo: FlightListRepo) : ViewModel() {
    private val filterData = FilterData()
    var totalFlightNumber = ObservableField<String>()
    val isShowSort = ObservableBoolean(false)
    val sortingObserver = ObservableField<SortingType>()
    var flightList = MutableLiveData<Event<List<Flights>>>()
    var airlineList = MutableLiveData<Event<List<Airline>>>()
    val dataState = MutableLiveData<Event<NetworkState>>()
    var allFlightList = listOf<Flights>()

    var filterParams = FilterParams()
    var flightSearchID = ""
    var flightSessionID = ""

    val showMessage = MutableLiveData<Event<String>>()
    lateinit var flightFilter: FlightFilter
    var isWithFilter: Boolean = false

    init {
        generatePassengers()
        getFlightList()
    }

    private fun generatePassengers() {
        viewModelScope.launch {
            repo.generatePassengerList()
        }
    }

    private fun getFlightList() {
        isWithFilter = false
        viewModelScope.launch {
            try {
                dataState.value = Event(NetworkState.LOADING)
                val response = repo.getFlightSearchResponse()
                flightSearchResponse(response)
            } catch (exception: STException) {
                exception.printStackTrace()
            }
        }
    }

    private fun getFlightListWithFilter(filterData: FilterData) {
        isWithFilter = true
        viewModelScope.launch {
            try {
                dataState.value = Event(NetworkState.LOADING)
                val response = repo.getFlightFilterResponse(filterData)
                flightSearchResponse(response)
            } catch (exception: STException) {
                exception.printStackTrace()
            }
        }
    }

    fun onRetryButtonClick() {
        if (isWithFilter) {
            getFlightListWithFilter(filterData)
        } else {
            getFlightList()
        }
    }

    private fun flightSearchResponse(data: GenericRestResponse<FlightSearchResponse>) {
        when (data) {
            is BaseResponse.Success -> {
                if (data.body.response.flights!!.isNotEmpty()) {
                    val flightSearchResponse = data.body.response

                    flightSearchResponse.flights?.map { flights ->
                        flights.segments?.map { segments ->
                            segments.segment?.map {
                                it.classType = flightSearchResponse.class_!!
                            }
                        }
                    }

                    totalFlightNumber.set(data.body.response.totalRecords.toString() + available_flights)
                    flightFilter = flightSearchResponse.filters!!
                    flightSearchID = flightSearchResponse.searchId!!
                    flightSessionID = flightSearchResponse.sessionId!!
                    allFlightList = flightSearchResponse.flights!!
                    flightList.value = Event(flightSearchResponse.flights)
                    airlineList.value = Event(flightSearchResponse.filters.airlines!!)
                    dataState.value = Event(NetworkState.LOADED)

                } else {
                    dataState.value = Event(NetworkState.emptyResponse(null))
                }
            }

            is BaseResponse.ApiError ->
                dataState.value = Event(NetworkState.error(data.errorBody.message))

            is BaseResponse.NetworkError ->
                dataState.value = Event(NetworkState.error(data.error.localizedMessage))

            is BaseResponse.UnknownError ->
                dataState.value = Event(NetworkState.error(data.error.localizedMessage))
        }
    }

    fun onSortingBtnClick(sortingType: SortingType) {
        if (sortingType == SortingType.NONE) {
            isShowSort.set(!isShowSort.get())
        } else {
            sortingObserver.set(sortingType)
            filterParams.sort = sortingType.value
            filterData.searchId = flightSearchID
            filterData.filter = filterParams
            getFlightListWithFilter(filterData)
        }
    }

    fun handleAirlineFilter(airlineList: ArrayList<String>) {
        filterParams.sort = sortingObserver.get().toString().toLowerCase()
        filterParams.airlines = airlineList
        filterData.searchId = flightSearchID
        filterData.filter = filterParams
        getFlightListWithFilter(filterData)
    }

    fun handleFilter(filterParams: FilterParams) {
        filterData.searchId = flightSearchID
        filterData.filter = filterParams
        getFlightListWithFilter(filterData)
    }
}
