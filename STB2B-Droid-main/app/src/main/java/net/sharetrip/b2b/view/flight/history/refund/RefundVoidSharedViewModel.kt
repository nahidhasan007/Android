package net.sharetrip.b2b.view.flight.history.refund

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import net.sharetrip.b2b.view.flight.history.model.Flight
import net.sharetrip.b2b.view.flight.history.model.FlightHistory
import net.sharetrip.b2b.view.flight.history.model.RefundEligibleTravellerResponse
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller

class RefundVoidSharedViewModel(application: Application) : AndroidViewModel(application){
    var flightHistory : FlightHistory? = null
    var refundEligibilityResponse : RefundEligibleTravellerResponse? = null

    var voidEligibilityResponse : RefundEligibleTravellerResponse? = null

    var selectedPassengers = MutableLiveData<ArrayList<ReissueTraveller>>(arrayListOf())

    var voidSelectedTraveller = MutableLiveData<ArrayList<ReissueTraveller>>(arrayListOf())
//    val selectedPassengers: LiveData<ArrayList<ReissueTraveller>>
//        get() = _selectedPassengers

    var selectedFlights = MutableLiveData<ArrayList<Flight>>(arrayListOf())



//    fun addSelectedFlights(flight: Flight) {
//        val temp = _selectedFlights.value
//        temp?.let { flightArrayList ->
//            flightArrayList.add(flight)
//            flightArrayList.sortBy { flight1 ->
//                flight1.departure?.dateTime
//            }
//            _selectedFlights.value = flightArrayList
//        }
//    }
//
//    fun removeFlight(flight: Flight) {
//        val temp = _selectedFlights.value
//        temp?.let {
//            it.remove(flight)
//            _selectedFlights.value = it
//        }
//    }
//
//    fun addSelectedTraveller(traveller: ReissueTraveller) {
////        Log.d(ReissueChangeDateSharedViewModel.TAG, "add selected traveller: $traveller")
////        if (traveller.id == null) {
////            throw IllegalStateException("Traveller Id must not be null!")
////        }
//        val temp = _selectedPassengers.value
//        temp?.let {
//            it.add(traveller)
//            _selectedPassengers.value = it
//        }
//    }
//
//    fun removeTraveller(traveller: ReissueTraveller) {
//        val temp = _selectedPassengers.value
//        temp?.let {
//            it.remove(traveller)
//            _selectedPassengers.value = it
//        }
//    }
//
//    fun clearViewModel() {
//        selectedFlights.value?.clear()
//        selectedPassengers.value?.clear()
//        refundEligibilityResponse = null
//    }
}