package com.example.layout

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController

class Register : Fragment() {


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_register, container, false)
        val loginText = view.findViewById<TextView>(R.id.checkLogin)
        loginText.setOnClickListener()
        {
            findNavController().navigate(R.id.action_register_to_login)
        }
        val card = view.findViewById<CardView>(R.id.cardRegister)
        card.setBackgroundResource(R.drawable.carddesign)
        return view
    }

}