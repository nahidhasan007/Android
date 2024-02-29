package net.sharetrip.b2b.view.flight.history.reissue_change_date.utils

import java.text.DecimalFormat


fun String?.isPhoneNumberValid(): Boolean {
    return !isNullOrEmpty() && this.matches(PHONE_VALIDATION_REGEX.toRegex()) && this.length >= 11
}

fun Double.twoDigitDecimal(): Double {
    val df = DecimalFormat("#.##")
    return try {
        df.format(this).toDouble()
    } catch (e: Exception) {
        this
    }
}

