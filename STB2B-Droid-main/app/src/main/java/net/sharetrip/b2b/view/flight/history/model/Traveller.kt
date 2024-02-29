package net.sharetrip.b2b.view.flight.history.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import net.sharetrip.b2b.util.Gender
import net.sharetrip.b2b.util.isDateFormatValid
import net.sharetrip.b2b.util.isValidName

@Parcelize
data class Traveller(
    @Transient
    var id: String?,  // WARNING: DEFINITELY NOT an id! looks like this id is misleading. more like flightHistory->travelType (child, adult, ...)
    var titleName: String? = "MR",
    @field:Json(name = "givenName")
    var firstName: String? = "",
    @field:Json(name = "surName")
    var lastName: String? = "",
    var gender: String = Gender.male,
    var nationality: String = "BD",
    var dateOfBirth: String = "",
    var passportNumber: String = "",
    var frequentFlyerNumber: String = "",
    var passportExpireDate: String = "",
    var seatPreference: String = "",
    var mealPreference: String = "",
    var wheelChair: String? = "",
    var passportCopy: String = "",
    var visaCopy: String = "",
    val eTicket: String? = null,
    val travellerType: String? = null,
    val email: String? = null,
    val mobileNumber: String? = null,
    @Transient
    var isChecked: Boolean = false,
    var primaryContact: String = "No",
) : Parcelable {
    fun hasRequiredData(isDomestic: Boolean): Boolean {
        return firstName.isValidName() && lastName.isValidName() && nationality.isValidName() && (isDomestic || passportNumber.isNotEmpty())
                && dateOfBirth.isDateFormatValid() && (gender == Gender.male || gender == Gender.female)
    }
}