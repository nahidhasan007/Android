package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PendingModification(
    val bookingCode: String,
    val parentReissueCode: String,
    val searchId: String,
    val travellers: List<Traveller>,
    val type: String
) : Parcelable