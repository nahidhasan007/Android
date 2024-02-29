package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

data class ReissueQuotationRequestBody(
    var cabinClass: String? = "",
    val bookingCode: String,
    val reissueCode: String? = null,
    val isNowShow: Boolean = true,
    val travellers: List<String>? = listOf(),
    val flights: List<ReissueFlightBody>? = listOf(),
//    val selection: List<String>? = listOf(),
)

