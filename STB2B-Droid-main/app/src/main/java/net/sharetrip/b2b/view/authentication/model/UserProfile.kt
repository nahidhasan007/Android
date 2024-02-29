package net.sharetrip.b2b.view.authentication.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class UserProfile(
    @PrimaryKey
    val username: String,
    val title: String,
    val firstName: String,
    val lastName: String,
    val gender: String?,
    val dob: String?,
    val designation: String?,
    val avatar: String?,
    val email: String,
    val mobileNumber: String?,
    val address: String?,
    val token: String
) : Parcelable
