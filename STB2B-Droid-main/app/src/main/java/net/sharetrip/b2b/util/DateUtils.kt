@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package net.sharetrip.b2b.util

import android.annotation.SuppressLint
import net.sharetrip.b2b.model.Calender
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    const val API_DATE_PATTERN = "yyyy-MM-dd"
    const val DISPLAY_COMMON_DATE_PATTERN = "dd-MM-yyyy"
    var DISPLAY_LONG_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm"
    var DISPLAY_DATE_PATTERN = "dd MMM yy"
    const val DISPLAY_DATE_AND_TIME_PATTERN = "dd MMM, yyyy HH:mm"
    const val DISPLAY_DATE_AND_TIME_PATTERN_WITH_AM_PM = "d MMM yy, h:mm a"
    const val HH_MM_MMM_DD_YYYY = "HH:mm MMM dd, yyyy"

    @Throws(ParseException::class)
    fun calculatePassengerAge(birthDate: String, onDate: String): Int {
        val calendar1 = Calendar.getInstance()
        calendar1.timeInMillis = parseDateToMillisecond(onDate)
        val currentYear = calendar1[Calendar.YEAR]
        val calendar2 = Calendar.getInstance()
        calendar2.timeInMillis = parseDateToMillisecond(birthDate)
        val birthYear = calendar2[Calendar.YEAR]
        var age = currentYear - birthYear
        if (calendar1[Calendar.DAY_OF_YEAR] < calendar2[Calendar.DAY_OF_YEAR]) {
            age--
        }
        return age
    }

    @Throws(ParseException::class)
    fun parseDateToMillisecond(dateString: String): Long {
        val dateFormat = SimpleDateFormat(API_DATE_PATTERN)
        val calendar = Calendar.getInstance()
        val date = dateFormat.parse(dateString)
        calendar.time = date
        return calendar.timeInMillis
    }

    fun revampingDateFormat(
        dateString: String, currentFormat: String,
        expectedFormat: String
    ): String {
        val existingDateFormat = SimpleDateFormat(currentFormat, Locale.US)
        val expectedDateFormat = SimpleDateFormat(expectedFormat, Locale.US)
        return try {
            val date = existingDateFormat.parse(dateString)
            return expectedDateFormat.format(date)
        } catch (exception: Exception) {
            String()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun changeDateFormat(from: String, to: String, date: String?): String {
        return try {
            if (date != null) {
                val parser = SimpleDateFormat(from)
                val formatter = SimpleDateFormat(to)
                formatter.format(parser.parse(date)!!)
            } else
                ""
        } catch (e: Exception) {
            "Error occurred!"
        }
    }

    @Throws(ParseException::class)
    @SuppressLint("SimpleDateFormat")
    fun changeDateFormatToMillisecond(fromDatePattern: String, date: String): Long {
        val dateFormat = SimpleDateFormat(fromDatePattern)
        val calendar = Calendar.getInstance()
        val millisecondDate = dateFormat.parse(date)
        calendar.time = millisecondDate!!
        return calendar.timeInMillis
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun stringToDate(date: String): Date? {
        return SimpleDateFormat(API_DATE_PATTERN).parse(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun apiToDisplayDateFormat(date: String): String {
        return try {
            val parser = SimpleDateFormat(API_DATE_PATTERN)
            val formatter = SimpleDateFormat(DISPLAY_DATE_PATTERN)
            formatter.format(parser.parse(date)!!)
        } catch (e: Exception) {
            String()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getApiDateFormat(num: Int): String {
        val mCalendar = getNextDateCalender(num)
        val mDateFormat = SimpleDateFormat(API_DATE_PATTERN)
        return mDateFormat.format(mCalendar.time)
    }

    private fun getNextDateCalender(days: Int): Calendar {
        val mCalendar = Calendar.getInstance()
        mCalendar.add(Calendar.DATE, days)
        return mCalendar
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat(API_DATE_PATTERN)
        return sdf.format(Date())
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun getCurrentDateWithMin(): String {
        val sdf = SimpleDateFormat(YYYY_MM_DD_HH_MM_SS)
        return sdf.format(Date())
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun millisecondsToString(dateInMilliseconds: Long?, dateFormat: String): String? {
        return try {
            val formatter = SimpleDateFormat(dateFormat)
            return formatter.format(Date(dateInMilliseconds!!))
        } catch (e: Exception) {
            String()
        }
    }

    fun getDateFormat(inputDate: String): Date {
        val format: DateFormat = SimpleDateFormat(YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH)
        return format.parse(inputDate)
    }

    fun increaseDay(dateInMilliseconds: String): Long {
        return changeDateFormatToMillisecond(API_DATE_PATTERN, dateInMilliseconds) + 86400000
    }

    fun getCalender(): Calender {
        val calender = Calendar.getInstance()
        return Calender(
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun getTimeAMPM(inputTime: String) : String {
        try {
            val inputFormat = SimpleDateFormat("HH.mm",Locale.US)
            val outFormat = SimpleDateFormat("hh.hh a",Locale.US)
            val date = inputFormat.parse(inputTime)
            return outFormat.format(date)
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        return ""
    }
}
