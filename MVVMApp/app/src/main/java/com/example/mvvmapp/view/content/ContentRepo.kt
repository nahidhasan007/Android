package com.example.mvvmapp.view.content

import androidx.lifecycle.LiveData
import com.example.mvvmapp.model.Content
import com.example.mvvmapp.network.local.ContentDao

class ContentRepo(private val quoteDao: ContentDao) {
    val readQuotes : LiveData<List<Content>> = quoteDao.getQuote()

    suspend fun insertQuote(quote: Content)
    {
        quoteDao.insertQuote(quote)
    }
}