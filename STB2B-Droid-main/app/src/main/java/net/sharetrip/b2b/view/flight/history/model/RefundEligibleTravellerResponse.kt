package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller

@Parcelize
data class  RefundEligibleTravellerResponse(
    val allSelectionMandatory: Boolean,
    val automationType: Boolean,
    val bookingCode: String,
    val bookingDetails: BookingDetails,
    val flights: List<Flight>? = null,
    val isFlyDatePassed: Boolean,
    val pendingModifications: List<PendingModification>? = null,
    val reissueCode: String,
    val skipSelection: Boolean,
    val travellers: List<ReissueTraveller>? = null
) : Parcelable