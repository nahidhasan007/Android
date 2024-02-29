package net.sharetrip.b2b.view.transaction.view.transactionlist

data class TransactionUIItemData(
    val amount: Double? = 0.0,
    val created_at: String? = "",
    val isDebit: Boolean,
    val reference: String? = "",
    val status: String? = "",
    val type: String? = "",
    val uuid: String? = "",
    val isDebitOrCredit: String? = ""
)
