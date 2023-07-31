package com.example.messutilities.model

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Members_Table")
data class Members(
    val name: String,
    val phone: String,
    val email: String? = null,
    val nid: String? = null
) : Parcelable
