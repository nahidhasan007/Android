package net.sharetrip.b2b.view.flight.booking.ui.regreso

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import net.sharetrip.b2b.util.*
import net.sharetrip.b2b.util.analytics.B2BEvent
import net.sharetrip.b2b.view.flight.booking.model.FlightSearch
import net.sharetrip.b2b.view.flight.booking.model.TravellersInfo

class ReturnVM : ViewModel() {
    var flightSearch = MutableLiveData<FlightSearch>()
    var moveToTravellers = MutableLiveData<Event<Pair<String, TravellersInfo>>>()
    val dates = ObservableField<List<String>>()
    var flightSearchData = FlightSearch(RETURN, flightDate = getRoundDTripDate())
    val promotionBannerImage: String = AppSharedPreference.b2bFlightPromotionImageUrl
    val isBannerAvailable: Boolean = promotionBannerImage.isBlank()

    init {
        flightSearch.value = flightSearchData
        dates.set(toList(flightSearchData.flightDate))
    }

    fun updateDate(date: ArrayList<String>) {
        dates.set(date)
        flightSearchData.flightDate = Gson().toJson(date)
        flightSearchData.travellersInfo = TravellersInfo()
        updateFlightSearch()
    }

    fun updateAirport(isOrigin: Boolean, iata: String) {
        if (isOrigin) {
            flightSearchData.origin = iata
        } else {
            flightSearchData.destination = iata
        }
        updateFlightSearch()
    }

    fun updateTravellers(travellersInfo: TravellersInfo) {
        flightSearchData.travellersInfo = travellersInfo
        updateFlightSearch()
    }

    fun onCLickSwapAirport() {
        val origin = flightSearchData.origin
        val destination = flightSearchData.destination

        flightSearchData.origin = destination
        flightSearchData.destination = origin

        updateFlightSearch()
    }

    fun onClickTravellers() {
        moveToTravellers.value =
            Event(Pair(flightSearchData.flightDate, flightSearchData.travellersInfo))
    }

    private fun updateFlightSearch() {
        flightSearch.value = flightSearchData
    }

    fun getSearchQuery(): FlightSearch {
        return flightSearchData
    }

    fun getFlightData(): Map<String, String> {
        val flightData = HashMap<String,String>()
        flightData[B2BEvent.FlightEvent.FLIGHT_DATA_COUNT_OF_ADULT] = flightSearchData.travellersInfo.numberOfAdult.toString()
        flightData[B2BEvent.FlightEvent.FLIGHT_DATA_COUNT_OF_CHILD] = flightSearchData.travellersInfo.numberOfChildren.toString()
        flightData[B2BEvent.FlightEvent.FLIGHT_DATA_FLIGHT_CLASS] = flightSearchData.travellersInfo.classType
        flightData[B2BEvent.FlightEvent.FLIGHT_DATA_TRIP_TYPE] = flightSearchData.tripType
        flightData[B2BEvent.FlightEvent.FLIGHT_DATA_ORIGIN_CITY] = flightSearchData.origin
        flightData[B2BEvent.FlightEvent.FLIGHT_DATA_DESTINATION_CITY] = flightSearchData.destination
        return flightData
    }

}
