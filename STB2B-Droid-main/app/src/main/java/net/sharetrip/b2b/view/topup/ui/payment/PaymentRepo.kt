package net.sharetrip.b2b.view.topup.ui.payment

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.network.PaymentEndPoint
import net.sharetrip.b2b.view.topup.model.AgencyBalance
import net.sharetrip.b2b.view.topup.model.PaymentHistoryData

class PaymentRepo(private val endPoint: PaymentEndPoint) {

    suspend fun getPaymentHistory(): GenericResponse<RestResponse<PaymentHistoryData>> {
        return endPoint.getPaymentHistory(0, 10)
    }

    suspend fun checkBalance(): GenericResponse<RestResponse<AgencyBalance>> {
        return endPoint.getAgencyBalance()
    }
}
