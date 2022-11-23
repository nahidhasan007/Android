package com.example.lifecycleactivity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import android.widget.Button

class NewInfo : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_info)
        val bundle:Bundle? = intent.extras
        val info = bundle?.getCharSequenceArrayList("UserInfo")
        if (info != null) {
            for (data in info)
            {
                println(data)
            }
        }
        val user = findViewById<TextView>(R.id.viewUser)
        user.text = info.toString()
        val res = info.toString()
       // Log.i(TAG, user.text as String)
        val next = findViewById<Button>(R.id.next)
        next.setOnClickListener() {
            Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Last::class.java)
            intent.putExtra("Data", res)
            startActivity(intent)
        }
    }



}