package com.example.firstfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstfragmentbtn = findViewById<Button>(R.id.button1)
        val secondfragmentbtn = findViewById<Button>(R.id.button2)

        firstfragmentbtn.setOnClickListener(){
           replaceFragment(new fragment1());
        }
        secondfragmentbtn.setOnClickListener(){
            replaceFragment(new fragment2());
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        FragmentManager fragmentManager = getsupportfragmentManager()


    }


}