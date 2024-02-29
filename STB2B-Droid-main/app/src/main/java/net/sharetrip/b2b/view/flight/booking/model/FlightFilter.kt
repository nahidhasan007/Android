package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlightFilter (
    val cabin: List<Cabin>?,
    val price: Price?,
    val stop: List<Stop>,
    val airlines: List<Airline>?,
    val origin: List<OriginAirport>?,
    val destination: List<OriginAirport>?,
    val layover: List<OriginAirport>?,
    val weight: List<Weight>,
    @field:Json(name = "outbound")
    val outboundList: List<Outbound>,
    @field:Json(name = "return")
    val returnList: List<Return>,
    var refundable: Boolean?,
    var isRefundable: List<Refundable>
) : Parcelable{
    fun getFlightFilterModel(): List<Refundable> {
        return  isRefundable
    }
}