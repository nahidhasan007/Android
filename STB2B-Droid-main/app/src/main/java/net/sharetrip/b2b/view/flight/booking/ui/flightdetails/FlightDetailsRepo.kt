package net.sharetrip.b2b.view.flight.booking.ui.flightdetails

import net.sharetrip.b2b.localdb.FlightSearchDao
import net.sharetrip.b2b.model.STException
import net.sharetrip.b2b.view.flight.booking.model.FlightSearch

class FlightDetailsRepo(private val flightSearchDao: FlightSearchDao) {

    suspend fun saveFlightSearch(flightSearch: FlightSearch) {
        try {
            flightSearchDao.saveFlightSearchQuery(flightSearch)
        } catch (throwable: Throwable) {
            throw STException("Fail to save flight search", throwable);
        }
    }
}