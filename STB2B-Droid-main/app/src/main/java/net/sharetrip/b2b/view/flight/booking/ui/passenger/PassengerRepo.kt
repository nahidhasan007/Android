package net.sharetrip.b2b.view.flight.booking.ui.passenger

import net.sharetrip.b2b.localdb.FlightSearchDao
import net.sharetrip.b2b.localdb.PassengerDao
import net.sharetrip.b2b.localdb.QuickPassengerDao
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.model.STException
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.toList
import net.sharetrip.b2b.view.flight.booking.model.ImageUploadResponse
import net.sharetrip.b2b.view.flight.booking.model.Passenger
import net.sharetrip.b2b.view.flight.booking.ui.flightdetails.FlightDetailsFragment
import net.sharetrip.b2b.view.more.model.QuickPassenger
import okhttp3.MultipartBody

class PassengerRepo(
    private val dao: PassengerDao,
    private val passengerId: String,
    private val flightEndPoint: FlightEndPoint,
    private val flightSearchDao: FlightSearchDao,
    private val quickPassengerDao: QuickPassengerDao
) {
    suspend fun updatePassenger(passenger: Passenger) {
        try {
            dao.savePassenger(passenger)
        } catch (throwable: Throwable) {
            throw STException("Fail to delete user", throwable);
        }
    }

    suspend fun getPassengerByID(): Passenger {
        try {
            return dao.getPassengerByID(passengerId)
        } catch (throwable: Throwable) {
            throw STException("Fail to get passenger", throwable);
        }
    }

    suspend fun sendFile(image: MultipartBody.Part, serviceTag: String): GenericResponse<RestResponse<ImageUploadResponse>> {
        return flightEndPoint.sendFile(image, serviceTag)
    }

    suspend fun getFlightDate(): String? {
        return try {
            val flightSearch = flightSearchDao.getFlight(FlightDetailsFragment.TRIP_TYPE)
            if (flightSearch != null) {
                toList(flightSearch.flightDate)?.get(0)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getQuickPassengerList(): List<QuickPassenger> {
        try {
            return quickPassengerDao.getQuickPassengerList()
        } catch (throwable: Throwable) {
            throw STException("Fail to get passenger", throwable);
        }
    }
}
