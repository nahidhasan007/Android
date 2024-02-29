package net.sharetrip.b2b.view.flight.history.reissue_change_date

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.view.flight.history.model.FlightHistory
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.FlightX
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueEligibilityResponse
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlight
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlightSearch
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller

class ReissueChangeDateSharedViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG: String = "ReissueChangeDateVm"
        var sharedViewModelInstanceCount = 0
    }

    init {
        sharedViewModelInstanceCount++
        Log.d(TAG, "sharedVmInstancecoUNT = $sharedViewModelInstanceCount")
    }

    val selectionQuotation: MutableLiveData<Event<Boolean>> = MutableLiveData(Event(false))

    private val _selectedFlights = MutableLiveData<ArrayList<ReissueFlight>>(arrayListOf())
    val selectedFlights: LiveData<ArrayList<ReissueFlight>> = _selectedFlights
    var reissueEligibilityResponse: ReissueEligibilityResponse? = null
    var flightHistory: FlightHistory? = null
    var isFullJourneySelect: Boolean = false  // todo: eta maybe kono api response er moddhe thakbe
//    var reissueEligibilityResponse: ReissueEligibilityResponse? = null

    val onSearchClick = MutableLiveData(Event(false))
    var totalChanges = 0
    var termsAndConditionCheckbox = MutableLiveData(false)
//    val automationSupported = MutableLiveData(Event(false))

    var bookingCode: String = ""
    var flightToBeBooked: FlightX? = null
    var flightSearch: ReissueFlightSearch? = null
    var manualFlights: List<ReissueFlight> = listOf()
    var totalTravellers: List<ReissueTraveller> = listOf()
    var subtitle = MutableLiveData("")
    var title = MutableLiveData("Date Change")
    var flightNoBefore: String = ""
    var quotationExpiryAt: String = ""
    var reissueSearchId = ""
    var reissueSequenceCode = ""
    var isALLSelected =  MutableLiveData(false)
    val isQuotationCalled = MutableLiveData(false)
    val manualCancelCheck = MutableLiveData(false)
    val reissueConfirmCheck = MutableLiveData(false)
    val quotationConfirmCheck = MutableLiveData(false)
    val reissueSuccess = MutableLiveData(false)

    private val _selectedPassengers = MutableLiveData<ArrayList<ReissueTraveller>>(arrayListOf())
    val selectedPassengers: LiveData<ArrayList<ReissueTraveller>>
        get() = _selectedPassengers

    fun addSelectedFlights(flight: ReissueFlight) {
        val temp = _selectedFlights.value
        temp?.let { flightArrayList ->
            flightArrayList.add(flight)
            flightArrayList.sortBy { flight1 ->
                flight1.departure?.dateTime
            }
            _selectedFlights.value = flightArrayList
        }
    }

    fun removeFlight(flight: ReissueFlight) {
        val temp = _selectedFlights.value
        temp?.let {
            it.remove(flight)
            _selectedFlights.value = it
        }
    }

    fun addSelectedTraveller(traveller: ReissueTraveller) {
        Log.d(TAG, "add selected traveller: $traveller")
        if (traveller.id == null) {
            throw IllegalStateException("Traveller Id must not be null!")
        }
        val temp = _selectedPassengers.value
        temp?.let {
            it.add(traveller)
            _selectedPassengers.value = it
        }
    }

    fun removeTraveller(traveller: ReissueTraveller) {
        val temp = _selectedPassengers.value
        temp?.let {
            it.remove(traveller)
            _selectedPassengers.value = it
        }
    }


    fun clearViewModel() {
        selectedFlights.value?.clear()
        selectedPassengers.value?.clear()
        reissueEligibilityResponse = null
    }

}