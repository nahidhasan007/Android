package com.example.mvvmapp.quote

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quote")
data class Quote(
        @PrimaryKey(autoGenerate = true)
        val id:Int,
        val book:String,
        val author:String

)