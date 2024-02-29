package net.sharetrip.b2b.view.more.quickPassengers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.view.more.model.QuickPassenger

class QuickPassengerVM(private val repo: QuickPassengerRepo) : ViewModel() {
    val passengerList = MutableLiveData<Event<List<QuickPassenger>>>()
    val dataFound = MutableLiveData<Event<Boolean>>()

    init {
        getPassengerList()
    }

    private fun getPassengerList() {
        viewModelScope.launch {
            val list = repo.getQuickPassengerList()
            if (list.isNotEmpty()) {
                dataFound.value = Event(true)
            }
            passengerList.value = Event(list)
        }
    }
}