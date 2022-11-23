package com.example.layouts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast

class NextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next)
        val bundle: Bundle? = intent.extras
        val nums = bundle?.getIntegerArrayList("Numbers")
        val txt = findViewById<TextView>(R.id.viewName)
        txt.text = nums.toString()
        //Toast.makeText(this, , Toast.LENGTH_SHORT).show()

    }
}