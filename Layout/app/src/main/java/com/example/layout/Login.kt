package com.example.layout

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController


class Login : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val signup = view.findViewById<TextView>(R.id.signUpText)
        signup.setOnClickListener()
        {
            findNavController().navigate(R.id.action_login_to_register)
        }
        val cardView = view.findViewById<CardView>(R.id.cardView)
        cardView.setBackgroundResource(R.drawable.carddesign)


        return view
    }


}