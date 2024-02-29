package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import net.sharetrip.b2b.util.DateUtil.getApiDateFormat
import net.sharetrip.b2b.util.DateUtil.tomorrowApiDateFormat
import net.sharetrip.b2b.view.flight.booking.model.Baggage
import net.sharetrip.b2b.view.flight.booking.model.ChildrenDOB
import net.sharetrip.b2b.view.flight.booking.model.MultiCityModel
import net.sharetrip.b2b.view.flight.history.model.AirFareRule
import java.util.Random


@Parcelize
data class ReissueFlightSearch(
    var originAddress: MutableList<String> = ArrayList(),
    var originCity: MutableList<String> = ArrayList(),
    var destinationCity: MutableList<String> = ArrayList(),
    var destinationAddress: MutableList<String> = ArrayList(),
    var depart: MutableList<String> = ArrayList(),
    var multiCityModels: MutableList<ReissueMultiCityModel> = mutableListOf(),
    var origin: MutableList<String> = ArrayList(),
    var destination: MutableList<String> = ArrayList(),
    var childDateOfBirthList: ArrayList<ChildrenDOB> = ArrayList(),
    var travellerBaggageCodes: ArrayList<TravellerBaggageCode> = ArrayList(),
    var baggage: List<Baggage>? = ArrayList(),
    var airFareRules: List<AirFareRule>? = ArrayList(),
    var classType: String = "",
    var tripType: String = "",
    var adult: Int = 0,
    var child: Int = 0,
    var infant: Int = 0,
    var searchId: String = "",
    var sequence: String = "",
    var sessionId: String = "",
    var fareDetails: String? = null,
    var stop: String? = null,
    var mCouponCode: String? = null,
    var paymentGateWayId: String? = null,
    var totalBaggageCost: Double = 0.0,
    var verifiedMobileNumber: String? = "",
    var otp: String? = ""
) : Parcelable {
    fun initForMultiCity() {
        origin.add("")
        origin.add("")

        originCity.add("")
        originCity.add("")

        originAddress.add("")
        originAddress.add("")

        destination.add("")
        destination.add("")

        destinationCity.add("")
        destinationCity.add("")

        destinationAddress.add("")
        destinationAddress.add("")

        classType = TravellerClassType.ECONOMY.type
        adult = 1
        child = 0
        infant = 0

        depart.add(tomorrowApiDateFormat)
        depart.add("")
        tripType = TripType.MULTI_CITY
    }

    fun initForRoundTrip() {
        init()
        var number = Random().nextInt(10)
        number = if (number == 0) 2 else number
        number = if (number == 1) 2 else number
        depart.add(getApiDateFormat(number))
        depart.add(getApiDateFormat(number + 7))
        tripType = TripType.ROUND_TRIP
    }

    fun initForOneWay() {
        init()
        depart.add(tomorrowApiDateFormat)
        tripType = TripType.ONE_WAY
    }

    private fun init() {
        origin.add("DAC")
        originCity.add("Dhaka")
        originAddress.add("Dhaka, Hazrat Shahjalal International Airport (DAC)")

        destination.add("JFK")
        destinationCity.add("New York")
        destinationAddress.add("New York, John F Kennedy International Airport (JFK)")

        classType = TravellerClassType.ECONOMY.type
        adult = 1
        child = 0
        infant = 0
    }

    val totalTravellers: Int
        get() = adult + child + infant
    var couponCode: String?
        get() = if (mCouponCode == null) "" else mCouponCode
        set(mCouponCode) {
            this.mCouponCode = mCouponCode
        }

    val numberOfTraveller: Int
        get() = adult + child + infant

    val childBirthListOnly: ArrayList<String>
        get() {
            val list: ArrayList<String> = ArrayList()
            for (child in childDateOfBirthList) {
                list.add(child.date)
            }
            return list
        }
}
