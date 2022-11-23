package com.example.layouts

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button2)
        button.setOnClickListener(){
            Log.i("MainActivity","Hello From Button!")
            Toast.makeText(this, "23 Years old", Toast.LENGTH_SHORT).show()
        }
        val next = findViewById<Button>(R.id.nextActivity)
        val btn = findViewById<Button>(R.id.listBtn)
        val numbers = arrayListOf<Int>(1,2,3,4,5)
        btn.setOnClickListener(){
            val name = findViewById<EditText>(R.id.person)
            val msg = name.text.toString()
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            val intent = Intent(this,NextActivity::class.java)
            intent.putExtra("Numbers",numbers)
            startActivity(intent)

        }


    }
}