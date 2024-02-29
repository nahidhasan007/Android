package net.sharetrip.b2b.view.flight.booking.ui.passengerlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.view.flight.booking.model.Passenger

class PassengerListVM(private val repo: PassengerListRepo, private val isDomestic: Boolean) :
    ViewModel() {
    val passengerList: LiveData<List<Passenger>>
        get() = _passengerList
    private val _passengerList = MutableLiveData<List<Passenger>>()

    val moveToNext: LiveData<Boolean>
        get() = _moveToNext
    private val _moveToNext = MutableLiveData<Boolean>()

    val showMessage: LiveData<Event<Boolean>>
        get() = _showMessage
    private val _showMessage = MutableLiveData<Event<Boolean>>()

    init {
        _moveToNext.value = false
        getPassengerList()
    }

    private fun getPassengerList() {
        viewModelScope.launch {
            val list = repo.getPassengerList()
            _passengerList.value = list
        }
    }

    fun updatePassengerList() {
        getPassengerList()
    }

    fun onClickNext() {
        for (passenger in passengerList.value!!) {
            if (!passenger.hasRequiredData(isDomestic)) {
                _showMessage.value = Event(true)
                return
            }
        }
        _moveToNext.value = true
    }

    fun onPause() {
        _moveToNext.value = false
    }
}
