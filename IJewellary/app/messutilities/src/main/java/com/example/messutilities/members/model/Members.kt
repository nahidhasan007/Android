package com.example.messutilities.members.model

data class Members(
    val name: String,
    val phone:String,
    val email:String?=null,
    val nid:String?=null
)
