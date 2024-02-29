package net.sharetrip.b2b.view.flight.booking.ui.verification

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.view.flight.booking.model.ContactInfo
import net.sharetrip.b2b.view.flight.booking.model.Passenger

class VerificationVM(private val repo: VerificationRepo) : ViewModel() {
    val passengerList: LiveData<Pair<List<Passenger>, String>>
        get() = _passengerList
    private val _passengerList = MutableLiveData<Pair<List<Passenger>, String>>()

    var contactInfo = ObservableField(ContactInfo())
    val isCheckedTermsAndCon = ObservableBoolean(false)

    init {
        getPassengerList()
        updateContactInfo()
    }

    private fun getPassengerList() {
        viewModelScope.launch {
            val list = repo.getPassengerList()
            val date = repo.getFlightDate()!!
            _passengerList.value = Pair(list, date)
        }
    }

    private fun updateContactInfo() {
        viewModelScope.launch {
            val contact = repo.getUserContact()
            contactInfo.set(contact)
        }
    }

    fun onCheckedChanged(isCheck: Boolean) {
        isCheckedTermsAndCon.set(isCheck)
    }
}
