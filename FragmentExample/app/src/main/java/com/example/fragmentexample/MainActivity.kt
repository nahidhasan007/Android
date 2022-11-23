package com.example.fragmentexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn1 = findViewById<Button>(R.id.fragment1btn)
        val btn2 = findViewById<Button>(R.id.fragment2btn)

        btn1.setOnClickListener() { replaceFragment(Fragment1()) }
        btn2.setOnClickListener() { replaceFragment(Fragment2()) }


        val frag1 = Fragment1()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,frag1).commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,fragment)
        fragmentTransaction.commit()
    }



}


