package net.sharetrip.b2b.view.more.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import net.sharetrip.b2b.util.Gender
@Parcelize
@Entity
data class QuickPassenger(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var titleName: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var gender: String? = Gender.male,
    var nationality: String? = "BD",
    var dateOfBirth: String? = "",
    var passportNumber: String? = "",
    var frequentFlyerNumber: String? = "",
    var passportExpireDate: String? = "",
    var seatPreference: String? = "",
    var mealPreference: String? = "",
    var wheelChair: String? = "",
    var passportCopy: String? = "",
    var visaCopy: String? = "",
    var travellerType: String? = "Adult",
    var email: String? = "",
    var mobileNumber: String? = ""
):Parcelable