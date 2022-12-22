package com.example.buttonclick

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var editText = findViewById<EditText>(R.id.appCompatEditText)
        val btn = findViewById<Button>(R.id.AddBtn)
        val list = findViewById<ListView>(R.id.TodoItemList)
        var listItems = ArrayList<String>()
        var adapter : ArrayAdapter<String>
        adapter = ArrayAdapter(this,
           android.R.layout.simple_list_item_1,listItems)
        list.adapter = adapter
        btn.setOnClickListener(){
            listItems.add(editText.text.toString())
            editText.setText("")
            adapter.notifyDataSetChanged()
        }


    }
}