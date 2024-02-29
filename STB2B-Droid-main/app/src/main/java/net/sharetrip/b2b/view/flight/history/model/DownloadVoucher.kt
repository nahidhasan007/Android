package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DownloadVoucher(
    var applyCustomPricing: Boolean = false,
    var attachCompany: Boolean = false,
    var bookingCode: String = "",
    val customPricing: CustomPricing = CustomPricing(),
    var hidePrice: Boolean = false,
    var pnrCode: String? = "",
    var showTotalPrice: Boolean = false
) : Parcelable


