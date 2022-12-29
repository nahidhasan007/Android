package com.example.retrofitapi.ui.list

import com.example.retrofitapi.model.Model

interface ClickHandler {
    fun postClicked(post:Model,position:Int)
}