package net.sharetrip.b2b.view.topup.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaymentHistory(
    val amount: Double?,
    val bank: String?,
    val branch: String?,
    val depositDate: String?,
    val depositedTo: String?,
    val gateway: String?,
    val givenAmount: Double?,
    val note: String?,
    val paymentMode: String?,
    val paymentStatus: String?,
    val reference: String?,
    val serviceCharge: Double?,
    val servicePercent: Double?
) : Parcelable
