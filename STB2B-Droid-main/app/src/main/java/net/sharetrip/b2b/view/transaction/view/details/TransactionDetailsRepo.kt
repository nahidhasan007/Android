package net.sharetrip.b2b.view.transaction.view.details

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.network.TransactionEndPoint
import net.sharetrip.b2b.view.transaction.model.TransactionDetails

class TransactionDetailsRepo(private val endPoint: TransactionEndPoint) {
    suspend fun transactionDetails(uuid: String): GenericResponse<RestResponse<TransactionDetails>> =
        endPoint.getTransactionDetails(uuid)
}