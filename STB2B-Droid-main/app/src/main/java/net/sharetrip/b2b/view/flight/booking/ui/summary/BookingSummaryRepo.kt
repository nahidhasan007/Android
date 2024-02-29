package net.sharetrip.b2b.view.flight.booking.ui.summary

import net.sharetrip.b2b.localdb.FlightSearchDao
import net.sharetrip.b2b.localdb.PassengerDao
import net.sharetrip.b2b.model.EmptyResponse
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.network.PaymentEndPoint
import net.sharetrip.b2b.view.flight.booking.model.BookingDetails
import net.sharetrip.b2b.view.flight.booking.model.FlightSearch
import net.sharetrip.b2b.view.flight.booking.model.Passenger
import net.sharetrip.b2b.view.flight.booking.ui.flightdetails.FlightDetailsFragment.Companion.TRIP_TYPE
import net.sharetrip.b2b.view.topup.model.AgencyBalance

class BookingSummaryRepo(
    private val flightSearchDao: FlightSearchDao, private val passengerDao: PassengerDao,
    private val flightEndPoint: FlightEndPoint,
    private val endPoint: PaymentEndPoint
) {
    suspend fun getFlightDetails(): FlightSearch {
        return flightSearchDao.getFlight(TRIP_TYPE)
    }

    suspend fun getPassengerList(): List<Passenger> {
        return passengerDao.getPassengerList()
    }

    suspend fun checkBalance(): GenericResponse<RestResponse<AgencyBalance>> {
        return endPoint.getAgencyBalance()
    }

    suspend fun bookFlight(bookingDetails: BookingDetails): GenericResponse<RestResponse<EmptyResponse>> {
        return flightEndPoint.flightBooking(bookingDetails)
    }
}
