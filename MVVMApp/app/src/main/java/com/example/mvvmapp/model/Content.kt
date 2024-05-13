package com.example.mvvmapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quote")
data class Content(
        @PrimaryKey(autoGenerate = true)
        val id:Int,
        val title:String,
        val url : String,
        val description:String

)