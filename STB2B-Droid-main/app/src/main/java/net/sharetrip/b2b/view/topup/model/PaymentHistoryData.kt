package net.sharetrip.b2b.view.topup.model

data class PaymentHistoryData(
    val data: List<PaymentHistory>,
    val offset: Int,
    val limit: Int,
    val count: Int
)