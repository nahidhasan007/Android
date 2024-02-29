package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Flights(
    val gds: String?,
    val sequence: String,
    val seatsLeft: Int?,
    val refundable: Boolean,
    val isRefundable: String,
    val instantPurchase: Boolean?,
    val weight: String?,
    val currency: String?,
    var originPrice: Double,
    var finalPrice: Double,
    val discountPercent: Double,
    val discountAmount: Double,
    val advanceIncomeTax: Double,
    val totalDuration: String?,
    val domestic: Boolean?,
    val baggage: List<Baggage>?,
    val flight: List<Flight>?,
    val segments: List<Segments>?,
    val price: List<PriceBreakdown>?
) : Parcelable {

    fun getTotalBaseFare() : Double {
        var totalBaseFare = 0.0
        price?.forEach {
            totalBaseFare += it.baseFare * it.numberPaxes
        }
        return totalBaseFare
    }

    fun getTotalTaxes(): Double {
        var totalTax = 0.0
        price?.forEach {
            totalTax += it.tax * it.numberPaxes
        }
        return totalTax
    }

    fun getTotalTraveller() : Int {
        var totalTraveller = 0
        price?.forEach {
            totalTraveller += it.numberPaxes
        }
        return totalTraveller
    }
}
