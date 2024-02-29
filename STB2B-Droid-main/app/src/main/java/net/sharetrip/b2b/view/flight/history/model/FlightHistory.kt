package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import net.sharetrip.b2b.util.DateUtils
import net.sharetrip.b2b.view.flight.booking.model.Baggage
import net.sharetrip.b2b.view.flight.booking.model.Flight
import net.sharetrip.b2b.view.flight.booking.model.Segments

@Parcelize
data class FlightHistory(
    val actualAmount: Double?,
    val airFareRules: List<AirFareRule>?,
    val airlineResCode: String?,
    val baggageInfo: List<Baggage>?,
    val bookingCode: String,
    val bookingCurrency: String?,
    val bookingStatus: String?,
    val cardSeries: String?,
    val conversionRate: Double?,
    val modificationHistories : List<ModifyHistory>? = null,
    val couponCode: String?,
    val discount: Double?,
    val discountType: String?,
    val eTicket: String?,
    val flight: List<Flight>,
    val gateWay: String?,
    val gatewayAmount: Double?,
    val gatewayCurrency: String?,
    val createdAt: String?,
    val lastCancellationTime: String?,
    val lastTicketingTime: String?,
    val paymentStatus: String?,
    val pnrCode: String?,
    val splitPnrCode: String?="",
    val pnrMessage: String?,
    val points: Points?,
    val priceBreakdown: PriceDetails?,
    val promotionalDiscount: Double?,
    val searchId: String?,
    val searchParamDetails: List<SearchParamDetail>?,
    val searchParams: SearchParams?,
    val segments: List<Segments>? ,
    val sequenceCode: String?,
    val sequenceNumber: Int?,
    val specialNote: String?,
    val totalAmount: Double?,
    val isModified : Boolean,
    val isReissueable: Boolean,
    val isVoidable: Boolean,
    val isRefundable: Boolean,
    val advanceIncomeTax: Double?,
    val domestic: Int?,
    var travellers: List<Traveller>?,
    val travellerCount: TravellerCount
) : Parcelable {

    val flightCode: String
        get() = when {
            flight.size == 1 -> {
                "${flight[0].originName?.code}-${flight[0].destinationName?.code}"
            }
            flight.size > 1 -> {
                val currentSize: Int = flight.size.minus(1)
                " ${flight[0].originName?.code}-${flight[currentSize].originName?.code}-${flight[currentSize].destinationName?.code}"
            }
            else -> {
                ""
            }
        }

    fun getFlyDate(): String {
        return if (flight.isNotEmpty()) {
            DateUtils.changeDateFormat(
                DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.DISPLAY_DATE_AND_TIME_PATTERN,
                flight[0].departureDateTime?.date!! + " " + flight[0].departureDateTime?.time!!
            )
        } else {
            ""
        }
    }

    fun getFlyDateAMPM(): String {
        return if (flight.isNotEmpty()) {
            DateUtils.changeDateFormat(
                DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.DISPLAY_DATE_AND_TIME_PATTERN_WITH_AM_PM,
                flight[0].departureDateTime?.date!! + " " + flight[0].departureDateTime?.time!!
            )
        } else {
            ""
        }
    }

    fun getTravellersInfo(): String {

        var travellersInfo = String()

        if (travellerCount.adult != 0) {
            travellersInfo = "${travellerCount.adult} Adt"
        }
        if (travellerCount.child != 0) {
            travellersInfo = "$travellersInfo ${travellerCount.child} Chd"
        }
        if (travellerCount.infant != 0) {
            travellersInfo = "$travellersInfo ${travellerCount.infant} Ift"
        }

        if (travellerCount.adult == 0 && travellerCount.child == 0 && travellerCount.infant == 0) {
            travellersInfo = "0"
        }

        return travellersInfo
    }

    fun setTravellerId() {
        var adult = 1
        var child = 1
        var infant = 1
        travellers?.map {
            when (it.travellerType) {
                "Adult" -> {
                    it.id = "${it.travellerType} $adult"
                    adult++
                }
                "Child" -> {
                    it.id = "${it.travellerType} $child"
                    child++
                }
                "Infant" -> {
                    it.id = "${it.travellerType} $infant"
                    infant++
                }
                else -> it.id = it.travellerType!!
            }
        }
    }
}


