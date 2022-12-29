package com.example.retrofitapi.ui.list


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitapi.model.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RetrofitModelView(private val repo : ListRepository) : ViewModel()
{   private var _checkPosts = MutableLiveData<List<Model>>()
    val checkPosts : LiveData<List<Model>>
        get() = _checkPosts
    init {
        getPosts()
    }
    private fun getPosts() {
        viewModelScope.launch(Dispatchers.IO) {
           val response = repo.getPosts()
            _checkPosts.postValue(response)
        }

    }


}