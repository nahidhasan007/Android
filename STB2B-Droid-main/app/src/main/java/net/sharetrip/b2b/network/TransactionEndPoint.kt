package net.sharetrip.b2b.network

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.view.transaction.model.TransactionDetails
import net.sharetrip.b2b.view.transaction.model.TransactionListData
import retrofit2.http.GET
import retrofit2.http.Query

interface TransactionEndPoint {

    @GET("transaction/details")
    suspend fun getTransactionDetails(
        @Query("uuid") uuid: String
    ): GenericResponse<RestResponse<TransactionDetails>>


    @GET("transaction/history")
    suspend fun getTransactionList(
        @Query("limit") limit : Int = 10,
        @Query("offset") offset : Int,
        @Query("filter", encoded = true) filter : String? = null
    ) : GenericResponse<RestResponse<TransactionListData>>

}