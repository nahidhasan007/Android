package com.example.fragmentexample

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val action = supportActionBar
        val color =  ColorDrawable(Color.parseColor("#FFFFFF"))
        action?.setBackgroundDrawable(color)

        val frag1 = Fragment1()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, frag1).commit()
    }

    }







