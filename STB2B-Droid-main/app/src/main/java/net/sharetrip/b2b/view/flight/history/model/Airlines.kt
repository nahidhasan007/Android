package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Airlines(
    val code: String,
    val full: String,
    val short: String
) : Parcelable