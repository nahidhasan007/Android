package com.example.lifecycleactivity

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i(TAG,"OnCreate Called")

        val name = findViewById<EditText>(R.id.name)
        val team = findViewById<EditText>(R.id.team)
        val post = findViewById<EditText>(R.id.post)
        val btn = findViewById<Button>(R.id.button)

        btn.setOnClickListener(){
            val userName = name.text.toString()
            val userTeam = team.text.toString()
            val userPost = post.text.toString()
            var userInfo = arrayListOf<String>()
            userInfo.add(userName)
            userInfo.add(userTeam)
            userInfo.add(userPost)
            Toast.makeText(this, userName, Toast.LENGTH_SHORT).show()
            val intent = Intent(this,NewInfo::class.java)
            intent.putExtra("UserInfo",userInfo)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG,"OnStart Called")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG,"ON Resume Called")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG,"Partially visible to user!")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"Not visible to user!")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG,"Activity is destroyed!")
    }

}