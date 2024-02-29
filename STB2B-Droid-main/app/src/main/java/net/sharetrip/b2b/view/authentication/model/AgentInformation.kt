package net.sharetrip.b2b.view.authentication.model

import android.os.Parcelable
import android.util.Patterns
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AgentInformation(
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var contactNumber: String = "",
    var userName: String = "",
    var password: String = "",
    var companyName: String = "",
    var establishedDate: String = "",
    var district: String = "",
    var companyAddress: String = ""
) : Parcelable {

    fun isValid(): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email)
            .matches() && password.length >= 8 && contactNumber.length >= 11 &&
                firstName != "" && lastName != "" && companyName != "" && district != "" && companyAddress != "" && userName != ""

    fun isValidInfo(): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email)
            .matches() && password.length >= 8 && contactNumber.length >= 11 &&
                firstName != "" && lastName != "" && userName != ""
}
