package net.sharetrip.b2b.view.flight.history.reissue_change_date.models
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.squareup.moshi.Json
import net.sharetrip.b2b.view.flight.booking.model.Airline
import net.sharetrip.b2b.view.flight.booking.model.Weight

data class Filters(
    val airlines: List<Airline>,
    val isRefundable: List<IsRefundable>?,
    val outbound: List<Outbound>?,
    val price: Price?,
    val `return`: List<Return>?,
    val weights: List<Weight>?
)

data class IsRefundable(
    val key: String,
    val value: Int
)

data class Outbound(
    @Json(name = "key")
    var key: String,
    @Json(name = "value")
    var value: String
)


@Parcelize
data class Price(
    @Json(name = "max")
    var max: Float,
    @Json(name = "min")
    var min: Float
) : Parcelable

data class Return(
    @Json(name = "key")
    var key: String,
    @Json(name = "value")
    var value: String
)


data class Weight(
    @Json(name = "key")
    var key: Int? = null,
    @Json(name = "weight")
    var weight: Int? = null,
    @Json(name = "unit")
    var unit: String? = null,
    @Json(name = "note")
    var note: String? = null
)