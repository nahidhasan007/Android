package net.sharetrip.b2b.view.flight.booking.model

data class FilterData (
    var searchId: String = "",
    var page: Int = 0,
    var filter: FilterParams? = null
)