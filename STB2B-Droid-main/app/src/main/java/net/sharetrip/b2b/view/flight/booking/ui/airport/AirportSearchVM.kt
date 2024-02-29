package net.sharetrip.b2b.view.flight.booking.ui.airport

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.*
import net.sharetrip.b2b.view.flight.booking.model.Airport
import net.sharetrip.b2b.view.flight.booking.ui.multicity.MultiCityFragment

class AirportSearchVM(bundle: Bundle?, private val repo: AirportSearchRepo) : ViewModel() {
    private val isOrigin = bundle?.getBoolean(ORIGIN_OR_DEST)
    val tripType = bundle?.getString(TRIP_TYPE)
    val position = bundle?.getInt(MULTI_CITY_POSITION, 0)

    val showMessage: LiveData<String>
        get() = _showMessage
    val _showMessage = MutableLiveData<String>()

    val airportList: LiveData<List<Airport>>
        get() = _airportList
    private val _airportList = MutableLiveData<List<Airport>>()

    val moveToBack: LiveData<String>
        get() = _moveToBack
    private var _moveToBack = MutableLiveData<String>()

    fun airportListByKeyText(queryText: String) {
        viewModelScope.launch {
            val data = repo.fetchAirportListByKeyWord(queryText)
            updateAirportList(data)
        }
    }

    fun updateAirports(airport: Airport) {
        viewModelScope.launch {
            if (tripType == OTHER) {
                if (isOrigin!!)
                    MultiCityFragment.multiCityList[position!!].origin = airport.iata
                else {
                    MultiCityFragment.multiCityList[position!!].destination = airport.iata
                    val size = MultiCityFragment.multiCityList.size
                    if(size - position > 1) {
                        MultiCityFragment.multiCityList[position+1].origin = airport.iata
                    }
                }
            }
            _moveToBack.value =  airport.iata
        }
    }

    private fun updateAirportList(response: GenericResponse<RestResponse<List<Airport>>>) {
        when (response) {
            is BaseResponse.Success -> _airportList.value = response.body.response

            is BaseResponse.ApiError -> _showMessage.value = response.errorBody.message

            is BaseResponse.NetworkError -> _showMessage.value = MsgUtils.networkErrorMsg

            is BaseResponse.UnknownError -> _showMessage.value = MsgUtils.unKnownErrorMsg
        }
    }
}
