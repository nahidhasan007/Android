package net.sharetrip.b2b.view.flight.booking.ui.multicity

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import net.sharetrip.b2b.util.*
import net.sharetrip.b2b.util.DateUtils.getCurrentDate
import net.sharetrip.b2b.util.analytics.B2BEvent
import net.sharetrip.b2b.view.flight.booking.model.FlightSearch
import net.sharetrip.b2b.view.flight.booking.model.MultiCityModel
import net.sharetrip.b2b.view.flight.booking.model.TravellersInfo
import java.util.*

class MultiCityVM : ViewModel() {
    var isAddButtonEnabled = ObservableBoolean(true)
    var isRemoveButtonEnabled = ObservableBoolean(false)
    var flightSearchData = FlightSearch(OTHER, flightDate = getCurrentDate())
    var travellersInfo = ObservableField<TravellersInfo>()
    val promotionBannerImage: String = AppSharedPreference.b2bFlightPromotionImageUrl
    val isBannerAvailable: Boolean = promotionBannerImage.isBlank()

    init {
        travellersInfo.set(TravellersInfo())
        initializeMultiCity()
    }

    fun updateTravellersAndClass(travellers: TravellersInfo) {
        travellersInfo.set(travellers)
        flightSearchData.travellersInfo = travellers
    }

    private fun initializeMultiCity() {
        val modelOne = MultiCityModel()
        modelOne.origin = MsgUtils.defaultAirportOriginCode
        modelOne.destination = MsgUtils.defaultAirportDestCode
        val date = Calendar.getInstance()
        date.add(Calendar.DATE, MULTI_CITY_INTERVAL_TO_DEPARTURE)
        modelOne.departDate = DateUtil.parseApiDateFormatFromMillisecond(date.timeInMillis)

        val modelTwo = MultiCityModel()
        modelTwo.origin = MsgUtils.defaultAirportDestCode
        modelTwo.destination = MsgUtils.defaultAirportOriginCode
        date.add(Calendar.DATE, MULTI_CITY_INTERVAL_TO_DEPARTURE)
        modelTwo.departDate = DateUtil.parseApiDateFormatFromMillisecond(date.timeInMillis)

        MultiCityFragment.multiCityList.add(modelOne)
        MultiCityFragment.multiCityList.add(modelTwo)
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
