package net.sharetrip.b2b.view.flight.booking.model

import android.util.Patterns

data class MailDetails(
    var recipent: String = "",
    var cc: String = "",
    var bcc: String = "",
    var subject: String = "",
    var messageBody: String = "",
) {
    fun isValid(hasCC: Boolean, hasBcc: Boolean): Boolean {
        return if (hasCC && hasBcc) {
            Patterns.EMAIL_ADDRESS.matcher(recipent)
                .matches() && Patterns.EMAIL_ADDRESS.matcher(cc)
                .matches() && Patterns.EMAIL_ADDRESS.matcher(bcc).matches()
        } else if (!hasCC && hasBcc) {
            Patterns.EMAIL_ADDRESS.matcher(recipent)
                .matches() && Patterns.EMAIL_ADDRESS.matcher(bcc).matches()
        } else if (hasCC && !hasBcc) {
            Patterns.EMAIL_ADDRESS.matcher(recipent)
                .matches() && Patterns.EMAIL_ADDRESS.matcher(cc).matches()
        } else {
            Patterns.EMAIL_ADDRESS.matcher(recipent).matches()
        }
    }

}