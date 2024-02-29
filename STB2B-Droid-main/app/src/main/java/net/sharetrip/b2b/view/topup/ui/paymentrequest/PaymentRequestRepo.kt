package net.sharetrip.b2b.view.topup.ui.paymentrequest

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.network.PaymentEndPoint
import net.sharetrip.b2b.view.flight.booking.model.ImageUploadResponse
import net.sharetrip.b2b.view.topup.model.GateWay
import okhttp3.MultipartBody

class PaymentRequestRepo(
    private val paymentEndPoint: PaymentEndPoint,
    private val flightEndPoint: FlightEndPoint
) {
    suspend fun getPaymentGateWay(): GenericResponse<RestResponse<List<GateWay>>> {
        return paymentEndPoint.getPaymentGateWay()
    }

    suspend fun sendFile(image: MultipartBody.Part, serviceTag: String): GenericResponse<RestResponse<ImageUploadResponse>> {
        return flightEndPoint.sendFile(image, serviceTag)
    }
}
