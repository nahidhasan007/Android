package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Policy(
    val header: List<String>,
    val rules: List<Rule>
): Parcelable