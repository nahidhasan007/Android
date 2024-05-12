package com.example.mvvmapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quote")
data class Content(
        @PrimaryKey(autoGenerate = true)
        val id:Int,
        val book:String,
        val author:String

)