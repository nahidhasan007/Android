package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import com.squareup.moshi.Json
import net.sharetrip.b2b.view.topup.model.Currency

data class PaymentMethod(
    var id: String,
    var name: String,
    var code: String,
    var logo: Logo,
    var checked: Boolean = false,
    var series: List<Series>,
    val type: String?,
    val bin: Bin,
    @field:Json(name = "coupon_applicable")
    var isCouponAvailable: Boolean = false,
    @field:Json(name = "redeem_tripcoin_applicable")
    var isRedeemTripCoinApplicable: Boolean = false,
    @field:Json(name = "earn_tripcoin_applicable")
    var isEarnTripCoinApplicable: Boolean = false,
    var currency: Currency,
    var charge: Double
)
