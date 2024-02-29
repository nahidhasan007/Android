package net.sharetrip.b2b.view.flight.booking.model

import com.squareup.moshi.Json
import java.util.ArrayList

data class Schedule(
    var outbound: List<Outbound>,
    @field:Json(name = "return")
    var returnList: List<Return>
) {
    fun getReturn(): List<Return> {
        return returnList
    }
}
