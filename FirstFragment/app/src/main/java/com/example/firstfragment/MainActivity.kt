package com.example.firstfragment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_library.NahidLib

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NahidLib.showToast(this, "Hello Test!")
    }


}