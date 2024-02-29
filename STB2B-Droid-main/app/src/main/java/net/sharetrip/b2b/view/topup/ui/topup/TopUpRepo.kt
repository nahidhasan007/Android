package net.sharetrip.b2b.view.topup.ui.topup

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.network.PaymentEndPoint
import net.sharetrip.b2b.view.topup.model.GateWay
import net.sharetrip.b2b.view.topup.model.TopUp
import net.sharetrip.b2b.view.topup.model.TopUpResponse

class TopUpRepo(private val endPoint: PaymentEndPoint) {

    suspend fun getPaymentGateWay(): GenericResponse<RestResponse<List<GateWay>>> =
        endPoint.getPaymentGateWay()

    suspend fun topUp(topUp: TopUp): GenericResponse<RestResponse<TopUpResponse>> =
        endPoint.topUp(topUp)

}
