package com.example.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.button)
        val textView = findViewById<TextView>(R.id.textView)
        val editText = findViewById<EditText>(R.id.editText)
        btn.setOnClickListener(){
            val msg = editText.text.toString()
            val sharedPref = getSharedPreferences(
                getString(R.string.demo), Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("Message",msg)
            editor.apply()
            textView.text = ""



        }
        val getshared = getSharedPreferences(getString(R.string.demo),Context.MODE_PRIVATE)
        val getmsg = getshared.getString("Message","Default msg shown because of NUll! value")
        textView.text = getmsg
    }

}