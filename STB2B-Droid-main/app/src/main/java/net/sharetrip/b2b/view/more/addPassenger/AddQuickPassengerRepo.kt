package net.sharetrip.b2b.view.more.addPassenger

import net.sharetrip.b2b.localdb.QuickPassengerDao
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.model.STException
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.view.flight.booking.model.ImageUploadResponse
import net.sharetrip.b2b.view.more.model.QuickPassenger
import okhttp3.MultipartBody

class AddQuickPassengerRepo(private var dao: QuickPassengerDao?,
                            private val flightEndPoint: FlightEndPoint,) {

    suspend fun sendFile(image: MultipartBody.Part, serviceTag: String): GenericResponse<RestResponse<ImageUploadResponse>> {
        return flightEndPoint.sendFile(image, serviceTag)
    }

    suspend fun saveQuickPassenger(quickPassenger: QuickPassenger) {
        try {
            dao?.saveQuickPassenger(quickPassenger)
        } catch (throwable: Throwable) {
            throw STException("Fail to Save user", throwable)
        }
    }

    suspend fun deleteAllQuickPassenger() {
        try {
            dao?.deleteAllQuickPassenger()
        } catch (throwable: Throwable) {
            throw STException("Fail to Save user", throwable)
        }
    }
}