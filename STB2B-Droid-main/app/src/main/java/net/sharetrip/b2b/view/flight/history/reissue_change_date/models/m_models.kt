package net.sharetrip.b2b.view.flight.history.reissue_change_date.models

import android.os.Parcelable
import androidx.annotation.IntDef
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import net.sharetrip.b2b.util.Gender
import net.sharetrip.b2b.util.isDateFormatValid
import net.sharetrip.b2b.util.isValidName


data class M_Model(
    val foo: Int = 0
)

data class ReissueFlightSearchResponse(
    val automationSupported: Boolean?,
    val expiresAt: String? = "",
    val filters: Filters?,
    val flights: List<FlightX>,
    val limit: Int?,
    val page: Int?,
    val reissueSearchId: String,
    val totalRecords: Int?,
    val tripType: String?,
    val status: String?,
    val manualSearchId: String?
)

data class ReissueQuotationFilterResponse(
    val _filters: Filters,
    val flights: List<FlightX>,
    val limit: Int,
    val page: Int,
    val reissueSearchId: String
)

@Parcelize
data class ReissueManualCancelBody(
    val reissueSearchId : String
) : Parcelable

@Parcelize
data class ReissueManualCancelResponse(
    val code: String,
    val message: String,
    val response: Response
) : Parcelable
@Parcelize
data class ConfirmReissueBody(
    val reissueSearchId: String,
    val reissueSequenceCode : String
) : Parcelable


@Parcelize
data class Response(
    val message: String,
    val success: Boolean
) : Parcelable



/*
"id":"9979342470647",
"eTicket":"9979342470647",
"titleName":"MR",
"givenName":"JOHN",
"surName":"DOE",
"dateOfBirth":"2000-09-30T00:00:00.000Z",
"travellerType":"Adult",
"hash":"a571f0065d9e8df16b5184e7e6806958",
"paxNumber":"1.1",
"paxAssociated":null,
"hasPendingModification":false
*/
@kotlinx.android.parcel.Parcelize
data class ReissueTraveller(
    var id: String,
    val eTicket: String? = null,
    var titleName: String? = "MR",
    var givenName: String? = "",
    var surName: String? = "",
    var dateOfBirth: String? = "",
    val travellerType: String? = null,
    val hash: String? = "",
    val paxNumber: String? = "",
    val paxAssociated: String? = "",
    val hasPendingModification: Boolean? = false,

    var gender: String? = Gender.male,
    var nationality: String? = "BD",
    var passportNumber: String? = "",
    var frequentFlyerNumber: String? = "",
    var passportExpireDate: String? = "",
    var seatPreference: String? = "",
    var mealPreference: String? = "",
    var wheelChair: String? = "",
    var passportCopy: String? = "",
    var visaCopy: String? = "",
    val email: String? = null,
    val mobileNumber: String? = null,
    var primaryContact: String = "No",

    @Transient
    var isChecked: Boolean = false,
) : Parcelable {
    fun hasRequiredData(isDomestic: Boolean): Boolean {
        return givenName.isValidName() && surName.isValidName() && nationality.isValidName() && (isDomestic || (!passportNumber.isNullOrBlank()) )
                && dateOfBirth.isDateFormatValid() && (gender == Gender.male || gender == Gender.female)
    }
}


@Parcelize
data class ReissueQuotationRequestSelectResponse(
    val code: String,
    val message: String,
    val response: Response
) : Parcelable

@Parcelize
data class ReissueQuotationRequestSelectBody(
    val bookingCode: String,
    val manualSearchId: String,
    val sequenceCode: String
) : Parcelable


const val SEGMENT = 1
const val TRANSIT = 2
@Retention(AnnotationRetention.SOURCE)
@IntDef(SEGMENT, TRANSIT)
annotation class FlightTypeEnum


//  [{"uuid":"a041b694dee32b39d2abbb5c6eb6a7f0",
//  "originCode":"DAC",
//  "destinationCode":"SIN",
//  "origin":{"code":"DAC","city":"Dhaka","country":"Bangladesh","airport":"Hazrat Shahjalal International Airport","terminal":null},
//  "destination":{"code":"SIN","city":"Singapore","country":"Singapore","airport":"Singapore Changi Airport","terminal":null},
//  "departure":{"dateTime":"2023-09-30 23:55:00","timezone":"6"},
//  "isFlyDatePassed":false,
//  "flightStatusMsg":null,
//  "flightStatusOptions":[]}
//  ]
