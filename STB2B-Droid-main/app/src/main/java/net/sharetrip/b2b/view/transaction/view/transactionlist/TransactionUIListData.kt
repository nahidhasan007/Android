package net.sharetrip.b2b.view.transaction.view.transactionlist

data class TransactionUIListData(
    val data: List<TransactionUIItemData>? = null,
    val offset: Int? = null,
    val limit: Int? = null,
    val count: Int? = null
)
