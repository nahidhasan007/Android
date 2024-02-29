package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DownLoadReissueVoucher(
    var reissueCode: String = "",
    var hidePrice: Boolean = false,
    var attachCompany: Boolean = false,
    var applyCustomPricing: Boolean = false,
    var customPricing: PriceBreakdown
) : Parcelable