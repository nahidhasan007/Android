package com.example.fragmentexample

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactInfo (var img:Int, var name: String, var contact:String, var address:String) :
    Parcelable