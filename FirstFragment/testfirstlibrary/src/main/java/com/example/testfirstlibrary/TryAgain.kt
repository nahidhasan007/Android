package com.example.testfirstlibrary

import android.content.Context
import android.widget.Toast

object TryAgain {

    fun willYou(context: Context, msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}