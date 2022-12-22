package com.example.crud.admin

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Admin")
data class Superadmin(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val username:String,
    val password:String
) : Parcelable
