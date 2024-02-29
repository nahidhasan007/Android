package net.sharetrip.b2b.view.topup.ui.paymenthistory

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.network.PaymentEndPoint
import net.sharetrip.b2b.view.topup.model.PaymentHistoryData

class PaymentHistoryRepo(private val endPoint: PaymentEndPoint) {
    suspend fun getPaymentHistory(currentPage: Int): GenericResponse<RestResponse<PaymentHistoryData>> {
        return endPoint.getPaymentHistory(currentPage, 10)
    }
}
