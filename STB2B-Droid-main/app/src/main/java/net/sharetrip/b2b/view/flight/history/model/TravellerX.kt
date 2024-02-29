package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TravellerX(
    val dateOfBirth: String,
    val eTicket: String,
    val email: String,
    val emdTicket: String,
    val hash: String,
    val mobileNumber: String,
    val name: String,
    val passportNumber: String,
    val paxType: String
) : Parcelable