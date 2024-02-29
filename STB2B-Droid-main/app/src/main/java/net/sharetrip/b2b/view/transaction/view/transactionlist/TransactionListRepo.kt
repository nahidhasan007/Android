package net.sharetrip.b2b.view.transaction.view.transactionlist

import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.network.TransactionEndPoint
import net.sharetrip.b2b.view.transaction.model.TransactionListData

class TransactionListRepo(private val endPoint: TransactionEndPoint) {
    suspend fun transactionList( offset : Int, filter : String? = null) : GenericResponse<RestResponse<TransactionListData>> =
        endPoint.getTransactionList( offset = offset, filter = filter)
}