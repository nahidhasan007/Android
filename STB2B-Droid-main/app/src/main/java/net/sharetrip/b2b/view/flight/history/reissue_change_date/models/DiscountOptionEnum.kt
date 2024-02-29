package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import androidx.annotation.StringDef

const val EARN = "earnTC"
const val REDEEM = "redeemTC"
const val COUPON = "useCoupon"

@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@StringDef(EARN, REDEEM, COUPON)
annotation class FlightDiscountOptionEnum
