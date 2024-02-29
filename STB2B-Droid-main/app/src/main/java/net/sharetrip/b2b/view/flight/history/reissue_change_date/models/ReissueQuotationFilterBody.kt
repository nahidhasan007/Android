package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import net.sharetrip.b2b.view.flight.booking.model.FilterParams

data class ReissueQuotationFilterBody(
    var filters: FilterParams? = null,
    val limit: Int? = 10,
    var page: Int? = 0,
    var reissueSearchId: String? = "",
    var sortBy: String? = ""
)