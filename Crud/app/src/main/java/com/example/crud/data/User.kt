package com.example.crud.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "UserTable")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name:String,
    val phone:String,
    val address:String
) : Parcelable