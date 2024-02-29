package net.sharetrip.b2b.view.flight.history.reissue_change_date.searchairport

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.view.flight.booking.model.Airport
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService
import kotlin.concurrent.thread

class SearchAirportRepo(private val bookingApiService: ReissueApiService, private val airportDao: AirportDao) {

    suspend fun getAirports(searchText: String): GenericResponse<RestResponse<List<Airport>>> {
        return bookingApiService.getAirports(searchText)
    }

    fun insert(airport: Airport) {
        thread {
            try {
                airportDao.insert(airport)
            } catch (e : Exception) {

            }
        }
    }

    suspend fun getAirports() = airportDao.getAirports().asReversed()
}
