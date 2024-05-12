package com.example.mvvmapp.view.content

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmapp.base.BaseResponse
import com.example.mvvmapp.base.BaseViewModel
import com.example.mvvmapp.base.RestResponse
import com.example.mvvmapp.model.ApiCallingKey
import com.example.mvvmapp.model.Content
import com.example.mvvmapp.network.local.ContentDatabase
import com.example.mvvmapp.network.remote.DataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import kotlin.coroutines.CoroutineContext

class ContentViewModel(application: Application) : BaseViewModel() {

    val quotes: LiveData<List<Content>>
    private val repository: ContentRepo

    private var job: Job = Job()

    private val _contents = MutableLiveData<List<Content>>()
    val contents: LiveData<List<Content>> get() = _contents

    val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    init {
        val quoteDao = ContentDatabase.getDatabase(application).quoteDao()
        repository = ContentRepo(quoteDao)
        quotes = repository.readQuotes
    }

    fun startApiCalls() {
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                viewModelScope.launch(Dispatchers.IO) {
                    getVideoFromRemoteUrl()
                }
            }
        }, 0, 5 * 60 * 1000) // 5 minutes interval
    }

    fun stopApiCalls() {
        job.cancel()
    }

    fun insertQuote(quote: Content) {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.insertQuote(quote)
        }
    }

    fun getVideoFromRemoteUrl() {
        executeSuspendedCodeBlock(ApiCallingKey.REMOTE_VIDEO.name) {
            DataManager.remoteApiService.getVideoContent()
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when (operationTag) {
            ApiCallingKey.REMOTE_VIDEO.name -> {
                val response = (data.body as RestResponse<*>).response as List<Content>
                if (response != null) {
                    _contents.postValue(response)
                }
            }
        }
    }

    private fun insertContentIntoLocalDb() {
        viewModelScope.launch(Dispatchers.IO) {
            for (content in contents.value!!) {
                repository.insertQuote(content)
            }
        }
    }

}