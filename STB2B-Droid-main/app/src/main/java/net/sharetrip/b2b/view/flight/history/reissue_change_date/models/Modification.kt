package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Modification(
    val bookingCode: String,
    val parentReissueCode: String,
    val searchId: String,
    val travellers: List<Traveller>,
    val type: String
) : Parcelable