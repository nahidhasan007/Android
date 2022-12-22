package com.example.mvvmapp.quote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class QuoteViewModel (application: Application): AndroidViewModel(application){
    val quotes : LiveData<List<Quote>>
    private val repository:QuoteRepository
    init {
        val quoteDao = QuoteDatabase.getDatabase(application).quoteDao()
        repository = QuoteRepository(quoteDao)
        quotes = repository.readQuotes
    }
    fun insertQuote(quote: Quote) {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.insertQuote(quote)
        }
    }

}