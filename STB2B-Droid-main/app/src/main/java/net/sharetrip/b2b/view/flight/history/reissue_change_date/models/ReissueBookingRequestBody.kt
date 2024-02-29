package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

data class ReissueBookingRequestBody(
    var cardSeries: String? = "",
    var gateWay: String? = "",
    var reissueSearchId: String? = "",
    var reissueSequenceCode: String? = ""
)
