package com.example.mvvmapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mvvmapp.databinding.FragmentAddQuotesBinding
import com.example.mvvmapp.quote.Quote
import com.example.mvvmapp.quote.QuoteViewModel

class AddQuotesFragment : Fragment() {

    lateinit var quoteViewModel: QuoteViewModel
    lateinit var binding : FragmentAddQuotesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_add_quotes,container,false)
        quoteViewModel = ViewModelProvider(this)[QuoteViewModel::class.java]
        /*val addquoteBtn = view.findViewById<Button>(R.id.addQuoteBtn)
        val quote = view.findViewById<EditText>(R.id.quote)
        val author = view.findViewById<EditText>(R.id.author)
        */
        binding.addQuoteBtn.setOnClickListener()
        {
            val book = binding.quote.text.toString()
            val author = binding.author.text.toString()
            val quote = Quote(0,book,author)
            quoteViewModel.insertQuote(quote)
            findNavController().navigate(R.id.action_addQuotesFragment_to_quotesFragment2)
        }
        quoteViewModel.quotes.observe(viewLifecycleOwner){
            Log.i(TAG,it.toString())
        }
        /*addquoteBtn.setOnClickListener()
        {
            val quotes = Quote(0,quote.text.toString(), author.text.toString())
            quoteViewModel.insertQuote(quotes)
            findNavController().navigate(R.id.action_addQuotesFragment_to_quotesFragment2)
        }

         */

        return binding.root
    }


}