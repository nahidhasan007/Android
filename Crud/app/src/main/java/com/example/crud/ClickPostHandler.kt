package com.example.crud

import com.example.crud.data.User


interface ClickPostHandler {
    fun userClicked(post: User, position:Int)
}