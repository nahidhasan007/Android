package net.sharetrip.b2b.network

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.view.topup.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PaymentEndPoint {
    @GET("payment/gateway")
    suspend fun getPaymentGateWay(
    ): GenericResponse<RestResponse<List<GateWay>>>

    @POST("payment/topup")
    suspend fun topUp(
        @Body topUp: TopUp
    ): GenericResponse<RestResponse<TopUpResponse>>

    @GET("payment/history")
    suspend fun getPaymentHistory(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): GenericResponse<RestResponse<PaymentHistoryData>>

    @GET("agency/balance")
    suspend fun getAgencyBalance(
    ): GenericResponse<RestResponse<AgencyBalance>>
}
