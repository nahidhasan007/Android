package net.sharetrip.b2b.view.transaction.model

data class Transaction(
    val amount: Double,
    val created_at: String,
    val credit: Double,
    val debit: Double,
    val isDebit: Boolean,
    val reference: String,
    val source: String,
    val status: String,
    val type: String,
    val updated_at: String,
    val uuid: String
)  {
    fun isDebitOrCredit() : String = if (isDebit) "Debit" else "Credit"
}