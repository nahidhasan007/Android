package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Traveller(
    val eTicket: String,
    val emdTicket: String,
    val hash: String,
    val name: String
) : Parcelable