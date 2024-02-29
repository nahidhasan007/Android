package net.sharetrip.b2b.util

import java.text.DecimalFormat

fun Long.convertCurrencyToBengaliFormat(): String {
    var givenString = this.toString()
    return try {
        if (givenString.contains(",")) {
            givenString = givenString.replace(",", "")
        }

        val longVal: Long = givenString.toLong()
        val formatter = DecimalFormat("#,##,##,###")

        formatter.format(longVal)
    } catch (e: Exception) {
        e.printStackTrace()
        givenString
    }
}