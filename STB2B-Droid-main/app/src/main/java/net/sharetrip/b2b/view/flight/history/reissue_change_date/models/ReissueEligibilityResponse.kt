package net.sharetrip.b2b.view.flight.history.reissue_change_date.models


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.IgnoredOnParcel
import net.sharetrip.b2b.view.flight.history.model.ReissueSearch

@Parcelize
data class ReissueEligibilityResponse(
    val automationSupported: Boolean,
    val isNoShowReissue: Boolean,
    val isNoShowPeriodOver: Boolean,
    val isPartiallyFlown: Boolean,
    val isNoShowReissueable: Boolean,
    val bookingCode: String,
    val cabinClass: String,
    val cabinClassUpgradeable: Boolean,
    val flights: List<ReissueFlight>,
    val isFullJourneySelect: Boolean,
    val isJourneyChange: Boolean,
    val paxSelectionMsg: String = "",
    val paxSelectionType: String = "",
    val reissueSearch: ReissueSearch?=null,
    val selectFlightMsg: String = "",
    val skipSelection: Boolean,
    val travellers: List<ReissueTraveller>? = null,
    val pendingModifications: List<Modification>?=null,
    val isPartiallyPaid: Boolean,
) : Parcelable



@Parcelize
data class ReissueFlight(
//    val id: String,
    val uuid : String ="",
    val originCode: String?,
    val destinationCode: String?,
    val origin: TravelLocation?,
    val destination: TravelLocation?,
    val departure: Departure?,
    val flightStatusOptions : List<String>? = listOf()
): Parcelable {
    @IgnoredOnParcel
    var isFlightSelected : Boolean = false
}

@Parcelize
data class TravelLocation(
    val code: String?,
    val city: String?,
    val country: String?,
    val airport: String?,
    val terminal: String?
): Parcelable


@Parcelize
data class Departure(
    val dateTime: String?,
    val timeZone: String?
): Parcelable
