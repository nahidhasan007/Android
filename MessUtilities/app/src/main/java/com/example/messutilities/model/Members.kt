package com.example.messutilities.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Members(
    val name: String,
    val phone: String,
    val email: String? = null,
    val nid: String? = null
) : Parcelable
