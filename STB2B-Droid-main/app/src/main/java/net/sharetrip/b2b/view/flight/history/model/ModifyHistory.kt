package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModifyHistory(
    val accountsNote: String,
    val accountsStatus: String,
    val accountsUpdatedAt: String,
    val agencyNote: String,
    val agent: Agent,
    val airlineResCode: String,
    val automationType: String,
    val bookingCode: String,
    val `class`: String? = "",
    val company: Company,
    val depth: Int,
    val investigationMsg: String,
    val isExpired: Boolean,
    val isPayable: Boolean,
    val isPayableMsg: String,
    val isRefundable: Boolean,
    val isReissueable: Boolean,
    val isVoidable: Boolean,
    val issuedAt: String? = null,
    val lastTicketingTime: String,
    val lastTicketingTimeExpired: Boolean,
    val modificationType: String,
    val modifiedAt: String,
    val noteToAgent: String,
    val oldResultDetails: List<OldResultDetail>? = listOf(),
    val parentReissueCode: String,
    val paymentStatus: String,
    val pnrCode: String,
    val priceBreakdown: PriceBreakdown,
    val reissueCode: String? = null,
    val reissueResultDetails: List<ReissueResultDetail>? = listOf(),
    val route: List<String>,
    val specialNote: String,
    val status: String,
    val statusLabel: String,
    val totalAmount: Int,
    val travellers: List<TravellerX>
) : Parcelable