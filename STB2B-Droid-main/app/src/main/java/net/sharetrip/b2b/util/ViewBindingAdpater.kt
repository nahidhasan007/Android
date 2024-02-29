package net.sharetrip.b2b.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.google.android.material.textfield.TextInputLayout
import net.sharetrip.b2b.R

@BindingAdapter("android:loadImageBinder", "android:placeholder")
fun ImageView.loadImageBinder(url: String?, drawableResId: Int) {
    this.load(url) {
        error(drawableResId)
        fallback(drawableResId)
        placeholder(drawableResId)
    }
}

@BindingAdapter("android:setColor")
fun setColorForTextDrawable(view: AppCompatTextView, isActive: Boolean) {
    if (isActive) {
        view.setTextColor(view.context.getColor(R.color.primary))
        view.compoundDrawables.getOrNull(0)?.setTint(view.context.getColor(R.color.primary))
    } else {
        view.setTextColor(view.context.getColor(R.color.dark))
        view.compoundDrawables.getOrNull(0)?.setTint(view.context.getColor(R.color.dark))
    }
}

@BindingAdapter("android:setDrawableTint")
fun setDrawableTint(view: AppCompatTextView, isCancellable: Boolean) {
    if (isCancellable) {
        var drawable: Drawable? =
            ContextCompat.getDrawable(view.context, R.drawable.ic_checked_mono)
        drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(drawable, ContextCompat.getColor(view.context, R.color.light_green))
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
        view.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    } else {
        var drawable: Drawable? = ContextCompat.getDrawable(view.context, R.drawable.ic_close_24dp)
        drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(drawable, ContextCompat.getColor(view.context, R.color.red))
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN)
        view.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }
}

@BindingAdapter("android:setTintAndTextColor")
fun AppCompatTextView.setTintAndTextColor(isActive: Boolean) {
    if (isActive) {
        this.setTextColor(this.context.getColor(R.color.primary))
        this.setDrawableTint(R.color.primary)
    } else {
        this.setTextColor(this.context.getColor(R.color.dark))
        this.setDrawableTint(R.color.dark)
    }
}

@BindingAdapter("android:setImage", "android:fromWhere")
fun AppCompatImageView.setImage(isConfirmed: Boolean, fromWhere: String) {
    if (isConfirmed && fromWhere == MODIFICATION) {
        this.setImageResource(R.drawable.ic_flight_processing)
    } else if (isConfirmed) {
        this.setImageResource(R.drawable.ic_accept_108dp)
    } else {
        this.setImageResource(R.drawable.ic_cancel_108dp)
    }
}

@BindingAdapter("android:setConfirmationTitle", "android:fromWhere")
fun AppCompatTextView.setConfirmationTitle(isConfirmed: Boolean, fromWhere: String) {
    if (isConfirmed && fromWhere == MODIFICATION) {
        this.text = this.context.getString(R.string.your_request_processed)
    } else if (isConfirmed && fromWhere == ISSUE_TICKET) {
        this.text = this.context.getString(R.string.issued)
    } else if (!isConfirmed && fromWhere == MODIFICATION) {
        this.text = this.context.getString(R.string.your_request_declined)
    } else if (!isConfirmed && fromWhere == ISSUE_TICKET) {
        this.text = this.context.getString(R.string.issue_declined)
    } else if (isConfirmed && fromWhere == CANCEL_BOOKING) {
        this.text = this.context.getString(R.string.success)
    } else if (isConfirmed && fromWhere == CANCEL_BOOKING) {
        this.text = this.context.getString(R.string.success)
    } else if (!isConfirmed && fromWhere == CANCEL_BOOKING) {
        this.text = this.context.getString(R.string.cancel_declined)
    }
}

@BindingAdapter("android:setConfirmationMessage", "android:fromWhere")
fun AppCompatTextView.setConfirmationMessage(isConfirmed: Boolean, fromWhere: String) {
    if (isConfirmed && fromWhere == MODIFICATION) {
        this.text = this.context.getString(R.string.your_request_processed_message)
    } else if (isConfirmed && fromWhere == ISSUE_TICKET) {
        this.text = this.context.getString(R.string.booking_confirmation_message_greetings)
    } else if (!isConfirmed && fromWhere == MODIFICATION) {
        this.text = this.context.getString(R.string.please_try_again)
    } else if (!isConfirmed && fromWhere == ISSUE_TICKET) {
        this.text = this.context.getString(R.string.booking_declined_message)
    } else if (isConfirmed && fromWhere == CANCEL_BOOKING) {
        this.text = this.context.getString(R.string.cancel_request_is_placed)
    } else if (!isConfirmed && fromWhere == CANCEL_BOOKING) {
        this.text = this.context.getString(R.string.booking_declined_message)
    }
}

@BindingAdapter("android:setTextColor")
fun AppCompatTextView.setColor(status: String?) {
    status.let {
        this.text = status
        when (status) {
            "Declined" -> this.setTextColor(Color.parseColor("#DE1921"))
            "Pending" -> this.setTextColor(Color.parseColor("#F57F17"))
            "Approved" -> this.setTextColor(Color.parseColor("#43A046"))
        }
    }
}

@BindingAdapter("android:requiredHtmlText")
fun htmlText(view: TextInputLayout, text: String) {
    view.hint = fromHtml("$text<font color=\"#FF0000\">*</font>")
}

@BindingAdapter("android:setTitle")
fun AppCompatTextView.setTitle(pageNo: Int) {
    when (pageNo) {
        FIRST_INDEX -> this.text = this.context.getString(R.string.best_rates)
        SECOND_INDEX -> this.text = this.context.getString(R.string.availability)
        THIRD_INDEX -> this.text = this.context.getString(R.string.multiple_payment_methods)
    }
}

@BindingAdapter("android:setSubTitle")
fun AppCompatTextView.setSubTitle(pageNo: Int) {
    when (pageNo) {
        FIRST_INDEX -> this.text = this.context.getString(R.string.we_find_the)
        SECOND_INDEX -> this.text = this.context.getString(R.string.select_and_book)
        THIRD_INDEX -> this.text = this.context.getString(R.string.flexible_payment_methods)
    }
}

@BindingAdapter("android:reasonToModify")
fun reasonToModify(view: TextInputLayout, actionCode: Int) {
    when (actionCode) {
        TICKET_ACTION_REFUND -> view.hint = view.context.getString(R.string.describe_refund_reason)
        TICKET_ACTION_TEMPORARY_CANCEL -> view.hint =
            view.context.getString(R.string.describe_cancel_reason)
    }
}

@BindingAdapter("android:toolbarTitle")
fun Toolbar.setToolbarTitle(actionCode: Int) {
    when (actionCode) {
        TICKET_ACTION_TEMPORARY_CANCEL -> {
            this.title = context?.getString(R.string.temporary_cancel_ticket)
        }
        TICKET_ACTION_VOID -> {
            this.title = context?.getString(R.string.void_ticket)
        }
        TICKET_ACTION_REFUND -> {
            this.title = context?.getString(R.string.refund_ticket)
        }
    }
}

@BindingAdapter("android:setWelcomeCenterImage")
fun AppCompatImageView.setWelcomeCenterImage(pageNo: Int) {
    when (pageNo) {
        FIRST_INDEX -> this.setImageResource(R.drawable.ic_attractive_rates)
        SECOND_INDEX -> this.setImageResource(R.drawable.ic_tick_mark)
        THIRD_INDEX -> this.setImageResource(R.drawable.ic_card)
    }
}

@BindingAdapter("android:setWelcomeImage")
fun AppCompatImageView.setWelcomeImage(pageNo: Int) {
    when (pageNo) {
        FIRST_INDEX -> this.setImageResource(R.drawable.ic_welcome_first)
        SECOND_INDEX -> this.setImageResource(R.drawable.ic_welcome_second)
        THIRD_INDEX -> this.setImageResource(R.drawable.ic_welcome_third)
    }
}

@BindingAdapter("android:setAmountTextColor")
fun AppCompatTextView.setAmountTextColor(isDebit: Boolean?) {
    if(isDebit!!) {
        this.setTextColor(Color.parseColor("#F57F17"))
    }
    else this.setTextColor(Color.parseColor("#FF1882FF"))
}

