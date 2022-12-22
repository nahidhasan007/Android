package com.example.mvvmapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmapp.R
import com.example.mvvmapp.quote.QuoteViewModel
import com.google.android.material.tabs.TabLayout.TabGravity

class quotesFragment : Fragment() {
    lateinit var quoteViewModel: QuoteViewModel


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quotes, container, false)
        val quotetext = view.findViewById<TextView>(R.id.quoteText)
        val authorText = view.findViewById<TextView>(R.id.authorText)
        quoteViewModel = ViewModelProvider(this)[QuoteViewModel::class.java]
        quoteViewModel.quotes.observe(viewLifecycleOwner){
            for (data in it)
            {
                quotetext.text = data.author
                authorText.text = data.book
            }
        }

        return view
    }

}