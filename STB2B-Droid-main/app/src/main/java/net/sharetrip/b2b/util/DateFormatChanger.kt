package net.sharetrip.b2b.util

import android.annotation.SuppressLint
import net.sharetrip.b2b.util.DateUtil.API_DATE_PATTERN
import net.sharetrip.b2b.util.DateUtil.DISPLAY_DATE_PATTERN
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val DATA_FORMAT_T = "yyyy-MM-dd'T'HH:mm:ss.SSS"
const val DATA_FORMAT_WITH_HOUR_DAY_MONTH_YEAR = "HH:mm aa, dd MMM, yyyy"

const val API_DATE_PATTERN_WITH_TIME = "yyyy-MM-dd HH:mm:ss"
const val DATA_FORMAT_WITH_HOUR = "HH.mm EEE"

fun String.dateChangeToDDMMYYYY(): String? {
    val finalDate: String?
    val input = SimpleDateFormat("yyyy-MM-dd")
    val output = SimpleDateFormat("dd-MM-yyyy")

    finalDate = try {
        val oneWayTripDate = input.parse(this) // parse input
        output.format(oneWayTripDate)
    } catch (e: Exception) {
        ""
    }
    return finalDate
}

fun String.dateChangeToDDMMYYYYFromZ(): String? {
    val finalDate: String?
    val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val output = SimpleDateFormat("dd-MM-yyyy")

    finalDate = try {
        val oneWayTripDate = input.parse(this) // parse input
        output.format(oneWayTripDate)
    } catch (e: ParseException) {
        this
    }
    return finalDate
}

fun String.dateChangeToMMMDDYY(): String? {
    var finalDate: String? = null
    val input = SimpleDateFormat("yyyy-MM-dd")
    val output = SimpleDateFormat("EEE, MMMM dd, yyyy", Locale.US)

    finalDate = try {
        val oneWayTripDate = input.parse(this) // parse input
        output.format(oneWayTripDate)
    } catch (e: ParseException) {
        e.printStackTrace()
        this
    }
    return finalDate
}

fun String.dateChangeToDDMMYY(): String {
    var finalDate: String
    val input = SimpleDateFormat("yyyy-MM-dd")
    val output = SimpleDateFormat("MMM dd, yyyy")

    finalDate = try {
        val oneWayTripDate = input.parse(this) // parse input
        output.format(oneWayTripDate)
    } catch (e: ParseException) {
        e.printStackTrace()
        this
    }
    return finalDate
}

fun String.dateChangeToDDMMYYHistory(): String {
    val finalDate: String
    val input = SimpleDateFormat("MM/dd/yyyy")
    val output = SimpleDateFormat("MMM dd, yyyy")

    finalDate = try {
        val oneWayTripDate = input.parse(this) // parse input
        output.format(oneWayTripDate)
    } catch (e: ParseException) {
        this
    }
    return finalDate
}

fun String.parseDateForDisplayFromApi(): String {
    if (this.isNullOrEmpty()) return this
    val mApiDateFormat = SimpleDateFormat(API_DATE_PATTERN)
    val mDisplayDateFormat = SimpleDateFormat(DISPLAY_DATE_PATTERN)
    val mDate = mApiDateFormat.parse(this)
    return mDisplayDateFormat.format(mDate)
}

fun String.getProperFlightStartDate(bookTodayFlight: Boolean): Long {
    val bookingDate: Long = if (bookTodayFlight) 0 else 1
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    val currentDateTime = Date()
    val timeThresholdString = formatter.format(currentDateTime) + " " + this
    val timeDateThresholdFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
    val thresholdDateTime = timeDateThresholdFormatter.parse(timeThresholdString)
    return if (currentDateTime.after(thresholdDateTime)) {
        bookingDate + 1
    } else {
        bookingDate
    }
}

fun String.isValidCancellationFightTime(): Boolean {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val currentDateTime = Date()
        val cancellationTime = formatter.parse(this)
        currentDateTime.before(cancellationTime)
    } catch (e: Exception) {
        true
    }
}

fun Date.changeToString():String {
    val mDateFormat = SimpleDateFormat(API_DATE_PATTERN)
    return mDateFormat.format(this)
}

fun String.dateChangeToHourDDMMYYYYFromT(): String? {
    val finalDate: String?
    val input = SimpleDateFormat(DATA_FORMAT_T)
    val output = SimpleDateFormat(DATA_FORMAT_WITH_HOUR_DAY_MONTH_YEAR, Locale.US)
    // output : 06:50 AM, 22 Sep, 2020

    finalDate = try {
        val oneWayTripDate = input.parse(this)
        output.format(oneWayTripDate)
    } catch (e: ParseException) {
        this
    }
    return finalDate
}

@SuppressLint("SimpleDateFormat")
fun String.dateChangeToHourWeekdayFromT() : String? {
    val mApiDateFormat = SimpleDateFormat(API_DATE_PATTERN_WITH_TIME)
    val mDisplayDateFormat = SimpleDateFormat(DATA_FORMAT_WITH_HOUR)
    val mDate = mApiDateFormat.parse(this)
    return mDisplayDateFormat.format(mDate)
}
