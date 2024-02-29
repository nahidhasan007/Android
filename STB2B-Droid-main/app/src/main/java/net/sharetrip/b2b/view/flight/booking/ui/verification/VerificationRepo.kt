package net.sharetrip.b2b.view.flight.booking.ui.verification

import net.sharetrip.b2b.localdb.FlightSearchDao
import net.sharetrip.b2b.localdb.PassengerDao
import net.sharetrip.b2b.localdb.UserProfileDao
import net.sharetrip.b2b.util.toList
import net.sharetrip.b2b.view.flight.booking.model.ContactInfo
import net.sharetrip.b2b.view.flight.booking.model.Passenger
import net.sharetrip.b2b.view.flight.booking.ui.flightdetails.FlightDetailsFragment
import net.sharetrip.b2b.view.flight.booking.ui.flightdetails.FlightDetailsFragment.Companion.TRIP_TYPE

class VerificationRepo(private val passengerDao: PassengerDao, private val userDao: UserProfileDao,private val flightSearchDao: FlightSearchDao) {

    suspend fun getPassengerList(): List<Passenger> {
        val list = passengerDao.getPassengerList()
        if(list.isEmpty()) {
            val passenger = Passenger(id = "Adult 1")
            passengerDao.savePassenger(passenger)
            return passengerDao.getPassengerList()
        }
        return list
    }

    suspend fun getUserContact(): ContactInfo? {
        val user = userDao.getUserprofile()
        return if (user.mobileNumber?.isEmpty() == true) {
            ContactInfo("", user.email)
        } else {
            user.mobileNumber?.let { ContactInfo(it, user.email) }
        }
    }

    suspend fun getFlightDate(): String? {
        val flightSearch = flightSearchDao.getFlight(TRIP_TYPE)
        return toList(flightSearch.flightDate)?.get(0)
    }
}
