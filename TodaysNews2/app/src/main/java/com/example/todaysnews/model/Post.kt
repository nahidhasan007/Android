package com.example.todaysnews.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    val body: String,
    val id: Int,
    val title: String,
    @PrimaryKey
    val userId: Int
)