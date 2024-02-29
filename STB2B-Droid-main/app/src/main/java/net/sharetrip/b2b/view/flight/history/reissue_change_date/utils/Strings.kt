package net.sharetrip.b2b.view.flight.history.reissue_change_date.utils

import android.annotation.SuppressLint
import android.util.Patterns
import net.sharetrip.b2b.util.DateUtil
import net.sharetrip.b2b.util.DateUtil.AM_PM_FORMAT
import net.sharetrip.b2b.util.DateUtil.API_DATE_PATTERN
import net.sharetrip.b2b.util.DateUtil.API_DATE_PATTERN_SLASH
import net.sharetrip.b2b.util.DateUtil.API_DATE_PATTERN_WITHOUT_SEC
import net.sharetrip.b2b.util.DateUtil.API_DATE_PATTERN_WITH_TIME
import net.sharetrip.b2b.util.DateUtil.API_DATE_PATTERN_YEAR
import net.sharetrip.b2b.util.DateUtil.DATA_FORMAT_WITH_HOUR
import net.sharetrip.b2b.util.DateUtil.DISPLAY_DATE_PATTERN
import net.sharetrip.b2b.util.DateUtil.DISPLAY_DATE_PATTERN_FULL_YEAR
import net.sharetrip.b2b.util.DateUtil.DISPLAY_DATE_PATTERN_WITHOUT_YEAR
import net.sharetrip.b2b.util.DateUtil.DISPLAY_DATE_PATTERN_year
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Strings {
    const val LINE_BREAK = "\n"
    const val SPACE = " "

    @JvmStatic
    fun isBlank(string: CharSequence?): Boolean {
        return string == null || string.toString().trim { it <= ' ' }.isEmpty()
    }

    @JvmStatic
    fun isNull(string: CharSequence?): Boolean {
        return string == null || string.toString().trim { it <= ' ' }.isEmpty() || string == "null"
    }

    fun isProperEmail(string: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(string).matches()
    }
}

fun String.parseDateForDisplayFromApi(): String {
    if (this.isNullOrEmpty()) return this
    val mApiDateFormat = SimpleDateFormat(API_DATE_PATTERN)
    val mDisplayDateFormat = SimpleDateFormat(DISPLAY_DATE_PATTERN_FULL_YEAR)
    val mDate = mApiDateFormat.parse(this)
    return mDisplayDateFormat.format(mDate)
}

fun String.parseDateForDisplayFromApiFullYear(): String {
    if (this.isNullOrEmpty()) return this
    val mApiDateFormat = SimpleDateFormat(API_DATE_PATTERN_YEAR)
    val mDisplayDateFormat = SimpleDateFormat(DISPLAY_DATE_PATTERN_FULL_YEAR)
    val mDate = mApiDateFormat.parse(this)
    return mDisplayDateFormat.format(mDate)
}

fun String.parseDateForDisplayFromApiWithHalfYear(): String {
    if (this.isNullOrEmpty()) return this
    val mApiDateFormat = SimpleDateFormat(API_DATE_PATTERN)
    val mDisplayDateFormat = SimpleDateFormat(DISPLAY_DATE_PATTERN_year)
    val mDate = mApiDateFormat.parse(this)
    return mDisplayDateFormat.format(mDate)
}


fun String.parseDateForDisplayFromApiSlashWithHalfYear(): String {
    if (this.isNullOrEmpty()) return this
    val mApiDateFormat = SimpleDateFormat(API_DATE_PATTERN_SLASH)
    val mDisplayDateFormat = SimpleDateFormat(DISPLAY_DATE_PATTERN_year)
    val mDate = mApiDateFormat.parse(this)
    return mDisplayDateFormat.format(mDate)
}

fun String.parseDateForDisplayFromApiWithDayMonth(): String {
    if (this.isNullOrEmpty()) return this
    val mApiDateFormat = SimpleDateFormat(API_DATE_PATTERN_WITHOUT_SEC)
    val mDisplayDateFormat = SimpleDateFormat(DISPLAY_DATE_PATTERN_WITHOUT_YEAR)
    val mDate = mApiDateFormat.parse(this)
    return mDisplayDateFormat.format(mDate)
}


fun String.parseDateForDisplayFromApiWithSlash(): String {
    if (this.isNullOrEmpty()) return this
    val mApiDateFormat = SimpleDateFormat(API_DATE_PATTERN_SLASH)
    val mDisplayDateFormat = SimpleDateFormat(DISPLAY_DATE_PATTERN_year)
    val mDate = mApiDateFormat.parse(this)
    return mDisplayDateFormat.format(mDate)
}

fun String.parseDateForDisplayFromApiWithAMPM(): String {
    if (this.isNullOrEmpty()) return this
    val mApiDateFormat = SimpleDateFormat(API_DATE_PATTERN_WITH_TIME)
    val mDisplayDateFormat = SimpleDateFormat(AM_PM_FORMAT)
    val mDate = mApiDateFormat.parse(this)
    return mDisplayDateFormat.format(mDate)
}

fun String.parseDateDisplayFromApiWithAMPM(): String {
    if (this.isNullOrEmpty()) return this
    val mApiDateFormat = SimpleDateFormat(API_DATE_PATTERN_WITHOUT_SEC)
    val mDisplayDateFormat = SimpleDateFormat(AM_PM_FORMAT)
    val mDate = mApiDateFormat.parse(this)
    return mDisplayDateFormat.format(mDate)
}
fun getWeekDay(dateString: String?): String {
    val calendar = Calendar.getInstance()
    try {
        calendar.timeInMillis = DateUtil.parseDateToMillisecond(dateString)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val index = calendar[Calendar.DAY_OF_WEEK]
    return DateUtil.days[index - 1]
}

@SuppressLint("SimpleDateFormat")
fun String.dateChangeToHourWeekdayFromT(): String? {
    val mApiDateFormat = SimpleDateFormat(API_DATE_PATTERN_WITH_TIME)
    val mDisplayDateFormat = SimpleDateFormat(DATA_FORMAT_WITH_HOUR)
    val mDate = mApiDateFormat.parse(this)
    return mDisplayDateFormat.format(mDate)
}

fun getAMPMFromDate(date: String): String {
    val dateFormat = SimpleDateFormat("a", Locale.US)
    return dateFormat.format(date)
}


fun String.getDay(): String {
    var pos = 0
    for (i in indices) {
        if (this[i] == ' ') {
            pos = i
            break
        }
    }
    return substring(0, pos)
}

fun String.getMonth(): String {
    var startPos = 0
    var endPos = 0
    var startPosSet = false
    for (i in indices) {
        if (this[i] == ' ') {
            if (!startPosSet) {
                startPos = i
                startPosSet = true
            } else {
                endPos = i
                break
            }
        }
    }
    return substring(startPos, endPos)
}

fun String.getYear(): String {
    return "20${substringAfterLast('\'')}"
}


@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@JvmName("replaceFirstCharWithCharSequence")
public inline fun String.replaceFirstChar(transform: (Char) -> CharSequence): String {
    return if (isNotEmpty()) transform(this[0]).toString() + substring(1) else this
}

public fun Char.uppercase(locale: Locale): String = toString().toUpperCase(locale)


public inline fun Char.toTitleCase(): Char = titlecaseChar()
inline fun Char.titlecaseChar(): Char = Character.toTitleCase(this)

public fun Char.titlecase(locale: Locale): String {
    val localizedUppercase = uppercase(locale)
    if (localizedUppercase.length > 1) {
        return if (this == '\u0149') localizedUppercase else localizedUppercase[0] + localizedUppercase.substring(
            1
        ).toLowerCase()
    }
    if (localizedUppercase != uppercase(locale)) {
        return localizedUppercase
    }
    return titlecaseChar().toString()
}
