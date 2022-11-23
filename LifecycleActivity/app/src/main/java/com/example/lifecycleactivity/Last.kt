package com.example.lifecycleactivity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Last : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last)
        val bundle:Bundle? = intent.extras
        val data = bundle?.get("Data")
        val dta = findViewById<TextView>(R.id.data)
        dta.text = data.toString()


    }
}