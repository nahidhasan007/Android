package net.sharetrip.b2b.view.flight.history.reissue_change_date.searchairport

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.view.flight.booking.model.Airport
import net.sharetrip.b2b.widgets.BaseOperationalVm
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ApiCallingKey


class SearchAirportViewModel(private val searchAirportRepo: SearchAirportRepo) :
    BaseOperationalVm() {
    val airportList = MutableLiveData<List<Airport>>()

    init {
        fetchInitData()
    }

    private fun fetchInitData() {
        viewModelScope.launch {
            searchAirportRepo.getAirports()
            fetchTopAirportListWithLocal()
        }
    }

    fun fetchTopAirportListWithLocal() {
        executeSuspendedCodeBlock(ApiCallingKey.FetchAirport.name) {
            searchAirportRepo.getAirports("top")
        }
    }

    fun getAirportListByName(searchKey : String) {
        executeSuspendedCodeBlock(ApiCallingKey.FetchAirport.name) {
            searchAirportRepo.getAirports(searchKey)
        }
    }

    fun handleSelectedItem(airport: Airport) {
        val airportData = Airport(name = airport.name, city = airport.city, iata = airport.iata)
        try {
            insert(airportData)
        } catch (_: Exception) {
        }
    }

    private fun insert(airport: Airport) = insertAirport(airport)

    private fun insertAirport(airport: Airport) {
        searchAirportRepo.insert(airport)
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        if (operationTag == ApiCallingKey.FetchAirport.name) {
            airportList.value = (data.body as RestResponse<*>).response as List<Airport>
        }
    }
}
