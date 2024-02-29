package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VRRConfirmResponse(
    val code: String,
    val message: String,
    val response: Response? = null
) : Parcelable