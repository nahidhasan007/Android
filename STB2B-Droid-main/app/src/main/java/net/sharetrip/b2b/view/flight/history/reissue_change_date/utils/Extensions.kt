package net.sharetrip.b2b.view.flight.history.reissue_change_date.utils

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import net.sharetrip.b2b.R
import org.threeten.bp.DayOfWeek
import org.threeten.bp.temporal.WeekFields
import java.util.Locale
import kotlin.math.round

fun <T> Bundle.getParcelableCompat(key: String, klass: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelable(key, klass)
    } else {
        this.getParcelable(key) as T?
    }
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun Double.convertToK(): String = "" + round(this / 1000).toInt() + "K"

fun dpToPx(dp: Int, context: Context): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
        context.resources.displayMetrics
    ).toInt()

internal fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

internal fun Boolean?.orFalse(): Boolean = this ?: false

fun Context.getDrawableCompat(@DrawableRes drawable: Int) =
    ContextCompat.getDrawable(this, drawable)

fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))

fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.US).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}

fun GradientDrawable.setCornerRadius(
    topLeft: Float = 0F,
    topRight: Float = 0F,
    bottomRight: Float = 0F,
    bottomLeft: Float = 0F
) {
    cornerRadii = arrayOf(
        topLeft, topLeft,
        topRight, topRight,
        bottomRight, bottomRight,
        bottomLeft, bottomLeft
    ).toFloatArray()
}

fun Context.showToast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, text, duration).show()
}

fun Context.setRoundCornerShapeDrawable(
    view: View,
    topLeft: Int? = null,
    topRight: Int? = null,
    bottomLeft: Int? = null,
    bottomRight: Int? = null,
    color: Int
) {
    val builder: ShapeAppearanceModel.Builder = ShapeAppearanceModel.builder()
    topLeft?.let {
        builder.setTopLeftCorner(CornerFamily.ROUNDED, dpToPx(it, this).toFloat())
    }
    topRight?.let {
        builder.setTopRightCorner(CornerFamily.ROUNDED, dpToPx(it, this).toFloat())
    }
    bottomLeft?.let {
        builder.setBottomLeftCorner(CornerFamily.ROUNDED, dpToPx(it, this).toFloat())
    }
    bottomRight?.let {
        builder.setBottomRightCorner(CornerFamily.ROUNDED, dpToPx(it, this).toFloat())
    }

    val backgroundShapeModel = builder.build()
    view.background = MaterialShapeDrawable(backgroundShapeModel).apply {
        fillColor = ColorStateList.valueOf(color)
    }
}
/* todo: import later
fun Context.showRateDialog() {
    val dialog = Dialog(this, R.style.MyDynamicDialogTheme)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(R.layout.dialog_rate_us)
    dialog.setCancelable(false)
    val btnRateUs = dialog.findViewById<TextView>(R.id.btnRateUs)
    val btnNotNow = dialog.findViewById<TextView>(R.id.btnNotNow)
    btnRateUs.setOnClickListener {
        val appPackageName: String = this.packageName
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (anfe: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
        dialog.dismiss()
    }
    btnNotNow.setOnClickListener {
        dialog.dismiss()
    }
    dialog.show()
}

fun String.capitalize(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.ROOT
        ) else it.toString()
    }
}
*/

fun getCurrentVisibleFragment(navHostFragment: NavHostFragment): Fragment? {
    val fragmentManager: FragmentManager = navHostFragment.childFragmentManager
    val fragment: Fragment? = fragmentManager.primaryNavigationFragment
    return if (fragment is Fragment) {
        fragment
    } else null
}


fun EditText.showKeyboard() {
    if (requestFocus()) {
        (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        setSelection(text.length)
    }
}
