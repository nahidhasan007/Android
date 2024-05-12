package com.example.mvvmapp.view.content

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import com.example.mvvmapp.base.BaseResponse
import com.example.mvvmapp.base.BaseViewModel
import com.example.mvvmapp.model.Content
import com.example.mvvmapp.network.local.ContentDatabase
import kotlinx.coroutines.launch

class ContentViewModel (application: Application): BaseViewModel(){
    val quotes : LiveData<List<Content>>
    private val repository: ContentRepo
    init {
        val quoteDao = ContentDatabase.getDatabase(application).quoteDao()
        repository = ContentRepo(quoteDao)
        quotes = repository.readQuotes
    }
    fun insertQuote(quote: Content) {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.insertQuote(quote)
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        TODO("Not yet implemented")
    }

}