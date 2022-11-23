package com.example.recycleviewapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    var contactList = arrayListOf<ContactModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.contactList)
        contactList.add(ContactModel(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))
        contactList.add(ContactModel(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))
        contactList.add(ContactModel(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))
        contactList.add(ContactModel(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))
        contactList.add(ContactModel(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))
        contactList.add(ContactModel(R.drawable.sharetrip3,"Save more on international flight bookings!","Back from a 1000+ hotels and resorts right from our app","23 Nov'22"))

        val mLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = ContactAdapter(contactList)




    }
}