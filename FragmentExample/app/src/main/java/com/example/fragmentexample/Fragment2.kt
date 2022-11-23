package com.example.fragmentexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class Fragment2 : Fragment(R.layout.fragment2){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = layoutInflater.inflate(R.layout.fragment2,container,false)
        var inputText = arguments?.getString("Input_text")
        val outText = rootView.findViewById<TextView>(R.id.textView)
        outText.text = inputText
        return rootView
    }
}
