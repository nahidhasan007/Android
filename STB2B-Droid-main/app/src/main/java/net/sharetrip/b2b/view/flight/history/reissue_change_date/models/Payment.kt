package net.sharetrip.b2b.view.flight.history.reissue_change_date.models
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Payment(
    val cancelUrl: String?,
    val declineUrl: String?,
    val paymentUrl: String?,
    val successUrl: String?
) : Parcelable
