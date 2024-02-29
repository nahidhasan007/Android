package net.sharetrip.b2b.view.flight.history.model
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import net.sharetrip.b2b.view.flight.booking.model.Flight

// todo: delete later
@Parcelize
data class ReissueEligibilityResponse123(
    val automationSupported: Boolean,
    val bookingCode: String,
    val cabinClass: String,
    val cabinClassUpgradeable: Boolean,
    val flights: List<Flight>,
    val isFullJourneySelect: Boolean,
    val isJourneyChange: Boolean,
    val paxSelectionMsg: String = "",
    val paxSelectionType: String = "",
    val reissueSearch: ReissueSearch?=null,
    val selectFlightMsg: String = "",
    val skipSelection: Boolean,
    val travellers: List<Traveller>
) : Parcelable
