package net.sharetrip.b2b.util

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

object DateUtil {
    var API_DATE_PATTERN = "yyyy-MM-dd"
    var API_DATE_PATTERN_YEAR = "dd-MM-yyyy"
    var API_DATE_PATTERN_SLASH = "MM/dd/yyyy"
    var DISPLAY_COMMON_DATE_PATTERN = "dd-MM-yyyy"
    var DISPLAY_LONG_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    var DISPLAY_DATE_PATTERN = "d MMM ''yy"
    var DISPLAY_DATE_PATTERN_year = "d MMM yy"
    var DISPLAY_DATE_PATTERN_WITHOUT_YEAR = "d MMM"
    var DISPLAY_DATE_PATTERN_FULL_YEAR = "d MMM, yyyy"
    var API_DATE_PATTERN_WITH_TIME = "yyyy-MM-dd HH:mm:ss"
    var API_DATE_PATTERN_WITHOUT_SEC = "yyyy-MM-dd HH:mm"
    var DATA_FORMAT_WITH_HOUR = "HH:mm EEE"
    var AM_PM_FORMAT = "hh:mm a"

    private fun getTime(mTime: Long, mDateFormat: Int): String {
        val mCalendar = Calendar.getInstance()
        mCalendar.timeInMillis = mTime
        val df = DateFormat.getDateInstance(mDateFormat)
        return df.format(mCalendar.time)
    }

    @get:SuppressLint("SimpleDateFormat")
    val todayApiDateFormat: String
        get() {
            val mCalendar = todayDateCalender
            val mDateFormat = SimpleDateFormat(API_DATE_PATTERN)
            return mDateFormat.format(mCalendar.time)
        }

    @JvmStatic
    @get:SuppressLint("SimpleDateFormat")
    val tomorrowApiDateFormat: String
        get() {
            val mCalendar = tomorrowDateCalender
            val mDateFormat = SimpleDateFormat(API_DATE_PATTERN)
            return mDateFormat.format(mCalendar.time)
        }

    @get:SuppressLint("SimpleDateFormat")
    val dayAfterTomorrowApiDateFormat: String
        get() {
            val mCalendar = dayAfterTomorrowDateCalender
            val mDateFormat = SimpleDateFormat(API_DATE_PATTERN)
            return mDateFormat.format(mCalendar.time)
        }

    @get:SuppressLint("SimpleDateFormat")
    val dayAfterOneYearApiDateFormat: String
        get() {
            val mCalendar = Calendar.getInstance()
            mCalendar.add(Calendar.YEAR, 1)
            val mDateFormat = SimpleDateFormat(DISPLAY_COMMON_DATE_PATTERN)
            return mDateFormat.format(mCalendar.time)
        }

    val dayAfterTomorrowDateInMillisecond: Long
        get() = dayAfterTomorrowDateCalender.timeInMillis

    private val tomorrowDateCalender: Calendar
        get() {
            val mCalendar = Calendar.getInstance()
            mCalendar.add(Calendar.DATE, 1)
            return mCalendar
        }

    val dayAfterTomorrowDateCalender: Calendar
        get() {
            val mCalendar = Calendar.getInstance()
            mCalendar.add(Calendar.DATE, 2)
            return mCalendar
        }

    fun parseAPIDateTimeToMillisecond(mDateString: String?): Long {
        return if (!mDateString.isNullOrEmpty()) {
            val mDateFormat = SimpleDateFormat(API_DATE_PATTERN_WITH_TIME, Locale.ENGLISH)
            val mCalendar = Calendar.getInstance()
            val mDate = mDateFormat.parse(mDateString)
            mCalendar.time = mDate
            mCalendar.timeInMillis
        } else
            return System.currentTimeMillis()
    }

    @JvmStatic
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

    private val todayDateCalender: Calendar
        get() = Calendar.getInstance()

    @SuppressLint("SimpleDateFormat")
    fun parseApiDateFormatFromMillisecond(mMillisecond: Long): String {
        val mCalendar = Calendar.getInstance()
        mCalendar.timeInMillis = mMillisecond
        val mDateFormat = SimpleDateFormat(API_DATE_PATTERN)
        return mDateFormat.format(mCalendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    fun parseDisplayCommonDatePatternFromMillisecond(mMillisecond: Long): String {
        val mCalendar = Calendar.getInstance()
        mCalendar.timeInMillis = mMillisecond
        val mDateFormat = SimpleDateFormat(DISPLAY_COMMON_DATE_PATTERN)
        return mDateFormat.format(mCalendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun parseDateToMillisecond(mDateString: String?): Long {
        val mDateFormat = SimpleDateFormat(API_DATE_PATTERN)
        val mCalendar = Calendar.getInstance()
        val mDate = mDateFormat.parse(mDateString)
        mCalendar.time = mDate
        return mCalendar.timeInMillis
    }

    @JvmStatic
    @Throws(ParseException::class)
    fun getYearfromDate(dateString: String?): Int {
        val mDateFormat = SimpleDateFormat(API_DATE_PATTERN)
        val mCalendar = Calendar.getInstance()
        val mDate = mDateFormat.parse(dateString)
        mCalendar.time = mDate
        return mCalendar[Calendar.YEAR]
    }

    @JvmStatic
    @Throws(ParseException::class)
    fun getmonthfromDate(dateString: String?): Int {
        val mDateFormat = SimpleDateFormat(API_DATE_PATTERN)
        val mCalendar = Calendar.getInstance()
        val mDate = mDateFormat.parse(dateString)
        mCalendar.time = mDate
        return mCalendar[Calendar.MONTH]
    }

    @JvmStatic
    @Throws(ParseException::class)
    fun getdayfromDate(dateString: String?): Int {
        val mDateFormat = SimpleDateFormat(API_DATE_PATTERN)
        val mCalendar = Calendar.getInstance()
        val mDate = mDateFormat.parse(dateString)
        mCalendar.time = mDate
        return mCalendar[Calendar.DAY_OF_MONTH]
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun parseDisplayDateToMillisecond(mDateString: String?): Long {
        val mDateFormat = SimpleDateFormat(DISPLAY_COMMON_DATE_PATTERN)
        val mCalendar = Calendar.getInstance()
        val mDate = mDateFormat.parse(mDateString)
        mCalendar.time = mDate
        return mCalendar.timeInMillis
    }

    @JvmStatic
    @SuppressLint("SimpleDateFormat")
    fun parseDisplayDateFormatFromApiDateFormat(dateString: String?): String {
        return revampingDateFormatFromCurrentToGiven(
                dateString!!,
                DateFormatPattern.API_DATE_PATTERN,
                DateFormatPattern.DISPLAY_DATE_PATTERN
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun parseDisplayCommonDateFormatFromApiDateFormat(dateString: String?): String {
        return revampingDateFormatFromCurrentToGiven(
                dateString!!,
                DateFormatPattern.API_DATE_PATTERN,
                DateFormatPattern.DISPLAY_COMMON_DATE_PATTERN
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun parseApiDateFormatFromDisplayCommonDateFormat(dateString: String?): String {
        if (dateString == null || dateString.isEmpty()) return ""
        return revampingDateFormatFromCurrentToGiven(
                dateString,
                DateFormatPattern.DISPLAY_COMMON_DATE_PATTERN,
                DateFormatPattern.API_DATE_PATTERN
        )
    }

    fun isDateIsValid(date: String?): Boolean {
        val pattern = Pattern.compile("[0-9]{2}-[0-9]{2}-[0-9]{4}")
        return pattern.matcher(date).matches()
    }

    @SuppressLint("SimpleDateFormat")
    fun parseDisplayDateFormatFromApiDateFormatData(dateString: String?): String {
        return revampingDateFormatFromCurrentToGiven(
                dateString!!,
                DateFormatPattern.FLIGHT_API_DATE_PATTERN,
                DateFormatPattern.DISPLAY_DATE_PATTERN
        )
    }

    fun parseDisplayDayNameFormatFromApiDateFormat(dateString: String?): String {
        return revampingDateFormatFromCurrentToGiven(
                dateString!!,
                DateFormatPattern.API_DATE_PATTERN,
                DateFormatPattern.DISPLAY_DAY_PATTERN
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun parseDisplayDateFromDate(mDate: Date?): String {
        val mDisplayDateFormat = SimpleDateFormat(DISPLAY_DATE_PATTERN)
        return mDisplayDateFormat.format(mDate)
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun parseApiDateFormatFromDisplayDate(mDate: String?): String {
        return parseApiDateFormatFromMillisecond(parseDateToMillisecond(mDate))
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun parseDisplayDateMonthFormatFromApiDateFormat(mDateString: String?): String {
        val mApiDateFormat = SimpleDateFormat(API_DATE_PATTERN)
        val mDisplayDateFormat = SimpleDateFormat(DISPLAY_DATE_PATTERN)
        val mDate = mApiDateFormat.parse(mDateString)
        return mDisplayDateFormat.format(mDate)
    }

    fun parseDateFromMillisecond(mMillisecond: Long): Date {
        val mCalendar = Calendar.getInstance()
        mCalendar.timeInMillis = mMillisecond
        return mCalendar.time
    }

    @JvmStatic
    fun getNumberOfDay(dateString: String?): String {
        return dateString!!.substring(8)
    }

    @JvmStatic
    fun getMonthYear(dateString: String): String {
        var index = Integer.valueOf(dateString.substring(5, 7))
        return months[--index] + " ' " + dateString.substring(2, 4)
    }

    fun getWeekDay(dateString: String?): String {
        val calendar = Calendar.getInstance()
        try {
            calendar.timeInMillis = parseDateToMillisecond(dateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val index = calendar[Calendar.DAY_OF_WEEK]
        return days[index - 1]
    }

    @Throws(ParseException::class)
    fun getAge(birthDate: String?): Int {
        val calendar = Calendar.getInstance()
        val currentYear = calendar[Calendar.YEAR]
        calendar.timeInMillis = parseDateToMillisecond(birthDate)
        val birthYear = calendar[Calendar.YEAR]
        return currentYear - birthYear
    }

    @Throws(ParseException::class)
    fun getAgeForFlight(birthDate: String?, flightDate: String?): Int {
        val calendar1 = Calendar.getInstance()
        calendar1.timeInMillis = parseDateToMillisecond(flightDate)
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

    fun getMonthYearTrimmed(dateString: String): String {
        var index = Integer.valueOf(dateString.substring(5, 7))
        return months[--index] + " " + dateString.substring(2, 4)
    }

    fun getDayAfterTomorrowDateInMillisecondForVisa(): Long {
        return dayAfterTomorrowDateInMillisecond
    }

    @SuppressLint("SimpleDateFormat")
    fun parseDisplayDateFormatFromMilliSecond(millisecond: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millisecond
        val mDateFormat = SimpleDateFormat(API_DATE_PATTERN)
        val mDisplayDateFormat = SimpleDateFormat(DISPLAY_DATE_PATTERN)
        val time = mDateFormat.format(calendar.time)
        val mDate = mDateFormat.parse(time)
        return mDisplayDateFormat.format(mDate)
    }

    fun parseDisplayDateFormatFromLongDateString(mDateString: String): String {
        val mApiDateFormat = SimpleDateFormat(DISPLAY_LONG_DATE_PATTERN)
        val mApiDatePattern = SimpleDateFormat(API_DATE_PATTERN)
        val mDate = mApiDateFormat.parse(mDateString)
        return mApiDatePattern.format(mDate)
    }

    fun revampingDateFormatFromCurrentToGiven(
            dateString: String,
            currentFormat: DateFormatPattern,
            expectedFormat: DateFormatPattern
    ): String {
        val existingDateFormat = SimpleDateFormat(currentFormat.datePattern, Locale.US)
        val expectedDateFormat = SimpleDateFormat(expectedFormat.datePattern, Locale.US)
        return try {
            val date = existingDateFormat.parse(dateString)
            return expectedDateFormat.format(date)
        } catch (exception: Exception) {
            ""
        }
    }


    @SuppressLint("SimpleDateFormat")
    @Throws(ParseException::class)
    fun parseDateTimeToMillisecond(
        dateString: String?,
        time: String?,
        dateFormat: String = "MM/d/yyyy HH:mm"
    ): Long {
        return if (!dateString.isNullOrEmpty() && !time.isNullOrEmpty()) {
            val mDateFormat = SimpleDateFormat(dateFormat)
            val mCalendar = Calendar.getInstance()
            val mDate = mDateFormat.parse("$dateString $time")
            mCalendar.time = mDate
            mCalendar.timeInMillis
        } else
            System.currentTimeMillis()
    }

    fun parseDisplayDateFromDateForNewCalendarDot(mDate: org.threeten.bp.LocalDate?): String {
        val headerDateFormatter = org.threeten.bp.format.DateTimeFormatter.ofPattern(API_DATE_PATTERN)
        return headerDateFormatter.format(mDate)
    }

    @SuppressLint("SimpleDateFormat")
    fun parseDisplayDateFromDateForNewCalendar(mDate: org.threeten.bp.LocalDate?): String {
        val headerDateFormatter = org.threeten.bp.format.DateTimeFormatter.ofPattern(DISPLAY_DATE_PATTERN)
        return headerDateFormatter.format(mDate)
    }


    private val months = arrayOf("JAN", "FAB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
    val days = arrayOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
}

enum class DateFormatPattern(val datePattern: String) {
    API_DATE_PATTERN("yyyy-MM-dd"),
    DISPLAY_DATE_PATTERN_FOR_HOTEL("d MMM"),
    FLIGHT_API_DATE_PATTERN("MM/dd/yyyy"),
    DISPLAY_COMMON_DATE_PATTERN("dd-MM-yyyy"),
    DISPLAY_DATE_PATTERN("d MMM ''yy"),
    CIRIUM_API_DATE_PATTERN ("yyyy-MM-dd'T'HH:mm:ss.SSS"),
    DISPLAY_DATE_TIME_PATTERN("h:mm a, d MMM ''yy"),
    DISPLAY_DAY_PATTERN("EEEE"),
    API_DATE_TIME_PATTERN("yyyy-MM-dd HH:mm")
}