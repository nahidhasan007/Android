package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import net.sharetrip.b2b.util.DateUtils
import net.sharetrip.b2b.util.DateUtils.apiToDisplayDateFormat
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.util.toList
import java.text.ParseException

@Parcelize
@Entity(tableName = "flight_search_table")
data class FlightSearch(
    @PrimaryKey
    @ColumnInfo(name = "trip_type")
    var tripType: String,

    @ColumnInfo(name = "origin_airport")
    var origin: String = MsgUtils.defaultAirportOriginCode,

    @ColumnInfo(name = "destination_airport")
    var destination: String = MsgUtils.defaultAirportDestCode,

    @ColumnInfo(name = "flight_date")
    var flightDate: String = DateUtils.getCurrentDate(),

    @ColumnInfo(name = "travellers_info")
    var travellersInfo: TravellersInfo = TravellersInfo(),

    @ColumnInfo(name = "flights")
    var flights: Flights? = null,

    var searchId: String = "",

    var sessionId: String = "",
) : Parcelable {

    fun getToolbarTitle(): Spannable {
        val dest = toList(destination)
        val orig = toList(origin)
        val builder = SpannableStringBuilder()
        val destination = dest?.get(dest.size - 1)
        val spannableDestination = SpannableString(" -  $destination")
        val origin = orig!![0]
        builder.append("$origin ")
        builder.append(spannableDestination)
        return builder
    }

    fun getToolbarSubTitle(): Spannable {
        val builder = SpannableStringBuilder()
        val depart = toList(flightDate)
        try {
            if (depart != null) {
                val mStartDate = apiToDisplayDateFormat(depart[0])
                if (depart.size == 1) {
                    builder.append(mStartDate).append(", ")
                } else {
                    val mEndDate = apiToDisplayDateFormat(depart[depart.size - 1])

                    builder.append(mStartDate).append(" - ").append(mEndDate).append(" ")
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return builder
    }
}
