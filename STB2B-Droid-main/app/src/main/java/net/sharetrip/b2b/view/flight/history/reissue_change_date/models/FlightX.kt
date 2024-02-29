package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import net.sharetrip.b2b.view.flight.booking.model.Baggage

@Parcelize
data class FlightX(
    val attachment: Boolean,
    val attachmentMessage: String,
    val baggage: List<Baggage>,
    val currency: String,
    val gatewayCurrency: String,
    //val instantPurchase: Int,
    val isRefundable: String,
    val miniRules: MiniRules? =null,
    val priceBreakdown: PriceBreakdown,
    val providerCode: String,
    val refundable: Boolean,
    val sequenceCode:String?,
    val reissueSequenceCode: String? = null,
    val manualSequenceCode: String?,
    val seatsLeft: Int? = 0,
    val weight: String? = null,
    val segments: List<Segment>,
    val total: Int,
    val totalDuration: String,
    val totalDurationInMinutes: Int
) : Parcelable
