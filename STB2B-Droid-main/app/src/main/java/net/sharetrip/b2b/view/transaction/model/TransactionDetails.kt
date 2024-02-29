package net.sharetrip.b2b.view.transaction.model

data class TransactionDetails(
    val amount: Double? = null,
    val balance: Double? = null,
    val created_at: String? = null,
    val isDebit: Boolean? = null,
    val reference: String? = null,
    val source: String? = null,
    val status: String? = null,
    val ticketNumber: String? = null,
    val type: String? = null,
    val updated_at: String? = null,
    val userName: String? = null,
    val uuid: String? = null
)