package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import net.sharetrip.b2b.util.Gender
import net.sharetrip.b2b.util.isDateFormatValid
import net.sharetrip.b2b.util.isGivenNameValid
import net.sharetrip.b2b.util.isValidName

@Parcelize
@Entity
data class Passenger(
    @PrimaryKey
    @Transient
    @ColumnInfo(name = "id")
    var id: String,
    var titleName: String? = "MR",
    @field:Json(name = "givenName")
    var firstName: String = "",
    @field:Json(name = "surName")
    var lastName: String = "",
    var gender: String = Gender.male,
    var nationality: String = "BD",
    var dateOfBirth: String = "",
    var passportNumber: String = "",
    var frequentFlyerNumber: String = "",
    var passportExpireDate: String = "",
    var seatPreference: String = "",
    var mealPreference: String = "",
    var wheelChair: String = "",
    var passportCopy: String = "",
    var visaCopy: String = "",
    val eticket: String? = null,
    val travellerType: String? = null,
    val email: String? = null,
    val mobileNumber: String? = null
) : Parcelable {
    fun hasRequiredData(isDomestic: Boolean): Boolean {
        val isPassportRequired = if (!isDomestic) {
            passportNumber.isNotEmpty() && passportExpireDate.isDateFormatValid()
        } else {
            true
        }
        return firstName.isGivenNameValid() && lastName.isValidName() && nationality.isValidName() && isPassportRequired
                && dateOfBirth.isDateFormatValid() && (gender == Gender.male || gender == Gender.female)
    }
}
