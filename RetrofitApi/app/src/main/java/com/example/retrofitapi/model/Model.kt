package com.example.retrofitapi.model

data class Model(
    val postId: Int,
    val id: Int,
    val title: String?,
    val body: String,
    val name: String?,
    val email: String?
)