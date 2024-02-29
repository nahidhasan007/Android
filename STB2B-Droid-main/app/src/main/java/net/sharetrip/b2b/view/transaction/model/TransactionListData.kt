package net.sharetrip.b2b.view.transaction.model


data class TransactionListData(
    val data: List<Transaction>? = null,
    val offset: Int? = null,
    val limit: Int? = null,
    val count: Int? = null
)
