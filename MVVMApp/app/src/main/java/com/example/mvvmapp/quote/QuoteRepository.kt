package com.example.mvvmapp.quote

import androidx.lifecycle.LiveData

class QuoteRepository(private val quoteDao: QuoteDao) {
    val readQuotes : LiveData<List<Quote>> = quoteDao.getQuote()

    suspend fun insertQuote(quote: Quote)
    {
        quoteDao.insertQuote(quote)
    }
}