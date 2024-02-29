package net.sharetrip.b2b.view.transaction.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionFilter(
    var date_from: String="",
    var date_to: String="",
    var reference: String="",
    var source: String="",
    var status: String="",
    var type: String=""
): Parcelable