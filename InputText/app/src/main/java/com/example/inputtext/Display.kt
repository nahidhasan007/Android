package com.example.inputtext

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Display : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)
        val get_name = intent.extras?.get("name").toString()
        val text = findViewById<TextView>(R.id.textView)
        text.text = get_name


    }
}