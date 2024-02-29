package net.sharetrip.b2b.util

import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.sharetrip.b2b.R
import net.sharetrip.b2b.view.flight.booking.model.Flights
import net.sharetrip.b2b.view.flight.history.model.Traveller
import java.io.File
import java.util.*
import java.util.regex.Pattern

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String?.isValidName(): Boolean {
    try {
        if (this == null || this.isEmpty()) {
            return false
        }
        return RegexValidation.validRegex(this, NAME_REGEX)
    } catch (e: Exception) {
        return false
    }
}

fun String?.isGivenNameValid(): Boolean {
    try {
        if (this == null || this.isEmpty()) {
            return true
        }
        return RegexValidation.validRegex(this, NAME_REGEX)
    } catch (e: Exception) {
        return false
    }
}

fun String?.isNameExceedsCharLength(): Boolean {
    return try {
        this != null && this.length<=52
    } catch (e: Exception) {
        false
    }
}

fun String?.isPassportNumberValid(): Boolean {
    if (this == null || this.isEmpty())
        return false
    return RegexValidation.validRegex(this, PASSPORT_REGEX)
}

fun String?.isDateFormatValid(): Boolean {
    if(this == null) return false
    if (this.length > 10) return false
    val pattern =
        Pattern.compile("^(19|20)\\d\\d([- /.])(0[1-9]|1[012])\\2(0[1-9]|[12][0-9]|3[01])\$")
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun String.selectUserTitle(dob: String, onDate: String): String {
    val age = DateUtils.calculatePassengerAge(dob, onDate)
    return if (age <= 11 && this == Gender.male) {
        "MSTR"
    } else if (age > 11 && this == Gender.male) {
        "MR"
    } else if (age <= 11 && this == Gender.female) {
        "MISS"
    } else if (age > 11 && this == Gender.female) {
        "MS"
    } else {
        "MX"
    }
}

@BindingAdapter("android:loadImageWithTransformation")
fun ImageView.loadImageWithTransformation(url: String?) {
    this.load(url) {
        transformations(CircleCropTransformation())
    }
}

fun fromHtml(string: String): Spanned? {
    return if (Build.VERSION.SDK_INT >= 24) {
        Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT) // for 24 api and more
    } else {
        Html.fromHtml(string) // or for older api
    }
}

object Gender {
    const val male = "Male"
    const val female = "Female"

    fun genderIs(isMale: Boolean): String = if (isMale) {
        male
    } else {
        female
    }
}

fun formatToTwoDigit(input: Any): String {
    return String.format("%02d", input)
}

fun getRoundDTripDate(): String {
    var number = Random().nextInt(10)
    number = if (number == 0) 2 else number
    number = if (number == 1) 2 else number
    return Gson().toJson(
        listOf(
            DateUtils.getApiDateFormat(number),
            DateUtils.getApiDateFormat(number + 7)
        )
    )
}

fun getColoredString(text: String?): Spannable {
    val ssb = SpannableStringBuilder(text)
    val fcsRed = ForegroundColorSpan(Color.parseColor("#4C4C4C"))
    ssb.setSpan(fcsRed, 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return ssb
}

fun toList(string: String): ArrayList<String>? {
    var list = string
    if (!string.startsWith("[")) {
        list = Gson().toJson(arrayListOf(string))
    }
    val itemType = object : TypeToken<ArrayList<String>>() {}.type
    return Gson().fromJson<ArrayList<String>>(list, itemType)
}

fun flightListToString(list: ArrayList<Flights>): String {
    val type = object : TypeToken<ArrayList<Flights>>() {}.type
    return Gson().toJson(list, type)
}

fun toFlightList(string: String): List<Flights>? {
    val itemType = object : TypeToken<ArrayList<Flights>>() {}.type
    return Gson().fromJson<ArrayList<Flights>>(string, itemType)
}

fun travellersListToString(list: List<Traveller>): String {
    val type = object : TypeToken<ArrayList<Traveller>>() {}.type
    return Gson().toJson(list, type)
}

fun toTravellerList(string: String): List<Traveller>? {
    val itemType = object : TypeToken<ArrayList<Traveller>>() {}.type
    return Gson().fromJson<ArrayList<Traveller>>(string, itemType)
}

fun getFilePath(uri: Uri, activity: Activity): File {
    if (uri.toString().indexOf("file:///") > -1) {
        return File(uri.path!!)
    }

    var cursor: Cursor? = null
    try {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        cursor = activity.contentResolver.query(uri, filePathColumn, null, null, null)
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return File(cursor?.getString(columnIndex!!)!!)
    } finally {
        cursor?.close()
    }
}

fun setIndicatorPosition(
    layoutManager: GridLayoutManager,
    totalVisibleItem: Int,
    dotsCount: Int,
    dots: Array<ImageView?>,
    context: Context
) {
    val lastCompleteVisible = layoutManager.findLastCompletelyVisibleItemPosition() + 1

    val dotsSelected = if (lastCompleteVisible <= totalVisibleItem) {
        0
    } else {
        val valueOne = ((lastCompleteVisible - totalVisibleItem)) / 2
        val valueTwo = (lastCompleteVisible - totalVisibleItem) % 2
        valueOne + valueTwo
    }

    for (i in 0 until dotsCount) {
        dots[i]?.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.non_active_dot
            )
        )
    }

    if (dotsSelected < dotsCount) {
        dots[dotsSelected]!!.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.active_dot
            )
        )
    }
}

fun AppCompatTextView.setDrawableTint(color: Int) {
    var drawable = ContextCompat.getDrawable(this.context, R.drawable.ic_upload_32dp)
    val tintColor = ContextCompat.getColor(this.context, color)
    drawable = DrawableCompat.wrap(drawable!!)
    DrawableCompat.setTint(drawable.mutate(), tintColor)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    this.setCompoundDrawables(drawable, null, null, null)
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun getTravellersWithID(passengers: List<Traveller>): List<Traveller> {
    var adult = 1
    var child = 1
    var infant = 1
    passengers.map {
        when (it.travellerType) {
            "Adult" -> {
                it.id = "${it.travellerType} $adult"
                adult++
            }
            "Child" -> {
                it.id = "${it.travellerType} $child"
                child++
            }
            "Infant" -> {
                it.id = "${it.travellerType} $infant"
                infant++
            }
            else -> it.id = it.travellerType!!
        }
    }
    return passengers
}

fun getRequestStatusColor(context: Context, status: String?): Int {
    return if (status == null)
        ContextCompat.getColor(context, R.color.dark)
    else {
        when (status) {
            REQUEST_STATUS_PENDING -> ContextCompat.getColor(context, R.color.caution_main)
            REQUEST_STATUS_PROCESSING -> ContextCompat.getColor(context, R.color.info_main)
            REQUEST_STATUS_PROCESSED -> ContextCompat.getColor(context, R.color.light_green)
            REQUEST_STATUS_COMPLETED -> ContextCompat.getColor(context, R.color.light_green)
            REQUEST_STATUS_DECLINED_BY_ST -> ContextCompat.getColor(context, R.color.error_main)
            REQUEST_STATUS_DECLINED_BY_AGENCY -> ContextCompat.getColor(context, R.color.error_main)
            REQUEST_STATUS_REISSUE_REQUESTED -> ContextCompat.getColor(context, R.color.gray)
            REQUEST_STATUS_REISSUE_PRICE_UPDATED -> ContextCompat.getColor(context, R.color.gray)
            REQUEST_STATUS_REISSUE_PRICE_APPROVED -> ContextCompat.getColor(context, R.color.gray)
            REQUEST_STATUS_REFUND_PRICE_UPDATED -> ContextCompat.getColor(context, R.color.accent)
            REQUEST_STATUS_REFUND_PRICE_APPROVED -> ContextCompat.getColor(
                context,
                R.color.light_green
            )
            REQUEST_STATUS_PROCESSED_BY_FLIGHT_TEAM -> ContextCompat.getColor(
                context,
                R.color.light_green
            )
            else -> Color.RED
        }
    }
}