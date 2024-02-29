package net.sharetrip.b2b.view.flight.history.reissue_change_date.multicity

import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.util.DateUtil
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.view.flight.booking.model.ChildrenDOB
import net.sharetrip.b2b.view.flight.booking.model.TravellersInfo
import net.sharetrip.b2b.view.flight.history.reissue_change_date.flightsummary.FlightSummaryViewModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.CalenderData
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlight
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlightSearch
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueMultiCityModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.ARG_AIRPORT_ADDRESS
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.ARG_AIRPORT_CITY
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.ARG_AIRPORT_CODE
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.EXTRA_CHILD_DOB_LIST
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.EXTRA_NUMBER_OF_ADULT
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.EXTRA_NUMBER_OF_CHILDREN
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.EXTRA_NUMBER_OF_INFANT
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.EXTRA_TRIP_CLASS_TYPE
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.Strings
import net.sharetrip.b2b.widgets.BaseOperationalVm
import java.util.Calendar

class MultiCityViewModel(
    private var searchQueryHint: String,
    private val searchQueryForTo: String,
    private val searchQueryForFrom: String,
    private val searchQueryForDate: String,
    selectedFlights: ArrayList<ReissueFlight>
) : BaseViewModel() {

    private val _loadMulticityAdapter: MutableLiveData<Event<Boolean>> = MutableLiveData<Event<Boolean>>(Event(false))
    val loadMulticityAdapter: LiveData<Event<Boolean>> get() = _loadMulticityAdapter

//    private val flightEventManager =
//        AnalyticsProvider.flightEventManager(AnalyticsProvider.getFirebaseAnalytics())
    private var travellers: TravellersInfo
    private var startDateInMillisecond = 0L

    var multiCityTripSearchModel = ReissueFlightSearch()
    var travelerClass = ObservableField<String>()
    var travelerType = ObservableField<String>()
    var travelersCount = ObservableField<String>()
    var promotionalImage = ""
    var isAirportLayoutClicked = MutableLiveData<Event<Boolean>>()
    val isRemoveItem = MutableLiveData<Event<Boolean>>()
    val changeItemAtPos = MutableLiveData<Pair<Int, ReissueMultiCityModel>>()
    var clickedPos = -1
    val isCheckboxActive = MutableLiveData(false)

    init {
        multiCityTripSearchModel.initForMultiCity()

        multiCityTripSearchModel.multiCityModels = selectedFlights.map {
            ReissueMultiCityModel(
                origin = it.origin?.code.toString(),
                destination = it.destination?.code.toString(),
                departDate = it.departure.toString(),
                originAirport = it.origin?.airport,
                destinationAirport = it.destination?.airport,
                originCity = it.origin?.city,
                destinationCity = it.destination?.city
            )
        } as MutableList<ReissueMultiCityModel>
        updateModelData()

        _loadMulticityAdapter.value = Event(true)
        travelersCount.set("${multiCityTripSearchModel.adult + multiCityTripSearchModel.child + multiCityTripSearchModel.infant}")
        travelerType.set(if ((multiCityTripSearchModel.adult) > 1) "${multiCityTripSearchModel.adult} Adults" else "${multiCityTripSearchModel.adult} Adult")
        travelerClass.set("${multiCityTripSearchModel.classType}")

        travellers = if (multiCityTripSearchModel.depart.size > 0)
            TravellersInfo(
                multiCityTripSearchModel.classType,    1, 0, 0,
                arrayListOf(), arrayListOf(),  multiCityTripSearchModel.depart[0]
            )
        else
            TravellersInfo()
    }

    fun onFromItemClick(position: Int) {
        clickedPos = position
        searchQueryHint = searchQueryForFrom
        isAirportLayoutClicked.value = Event(true)
    }

    fun onToItemClick(position: Int) {
        clickedPos = position
        searchQueryHint = searchQueryForTo
        isAirportLayoutClicked.value = Event(false)
    }

    fun onDateItemClick(position: Int, origin: String, destination: String) {
        clickedPos = position
        startDateInMillisecond = getStartDateInMillisecond(position)
        searchQueryHint = searchQueryForDate

        if (multiCityTripSearchModel.multiCityModels[clickedPos].origin.isNotEmpty() && multiCityTripSearchModel.multiCityModels[clickedPos].destination.isNotEmpty())
            navigateWithArgument(
                PICK_DEPARTURE_DATE_REQUEST,
                CalenderData(
                    startDateInMillisecond,
                    mDateHintText = "Departure Date",
                    fromAirportCode = multiCityTripSearchModel.multiCityModels[clickedPos].origin,
                    toAirportCode = multiCityTripSearchModel.multiCityModels[clickedPos].destination
                )
            )
        else
            showMessage(PLEASE_SELECT_ORIGIN_DESTINATION)
    }

    fun onClickTermsAndCondition() {
        navigateWithArgument(GOTO_TERMS, "")
    }

    fun clickTermsAndCondition() {
        Log.d("LogD", "We are now in muliticity viewmodel terms checking ${isCheckboxActive.value}")
        isCheckboxActive.value = !isCheckboxActive.value!!
    }

    fun onTravelersAndClassCardViewClicked() {

        Log.d(BaseOperationalVm.TAG, "tripDate = ${travellers?.tripDate}")
        Log.d(BaseOperationalVm.TAG, "travellersInfo = ${travellers}")
        navigateWithArgument(GOTO_TRAVELLER, travellers)
    }

    fun onSearchFlightButtonClicked() {
        Log.d("multicityModel", "let's see what happens!")
//        flightEventManager.searchMultiCity()
        val dataSet = multiCityTripSearchModel.multiCityModels
        var error = false
        for (model in dataSet) {
            if (Strings.isBlank(model.origin)) {
                showMessage("Invalid Flying from")
                error = true
                break
            } else if (Strings.isBlank(model.destination)) {
                showMessage("Invalid Flying to")
                error = true
                break
            } else if (Strings.isBlank(model.departDate)) {
                showMessage("Invalid Date")
                error = true
                break
            }
        }

        if (!error) {
            navigateWithArgument(GOTO_FLIGHT_LIST, multiCityTripSearchModel)
        }
    }

    private fun getStartDateInMillisecond(position: Int): Long {
        val item = multiCityTripSearchModel.multiCityModels[position]
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        val mStartDateInMillisecond: Long
        val mStartDateString = item.departDate
        mStartDateInMillisecond = if (Strings.isBlank(mStartDateString)) {
            calendar.timeInMillis
        } else {
            calendar.timeInMillis
        }
        return mStartDateInMillisecond
    }

    fun handleTravellerData(data: Intent) {
        multiCityTripSearchModel.adult = data.getIntExtra(EXTRA_NUMBER_OF_ADULT, 1)
        multiCityTripSearchModel.child = data.getIntExtra(EXTRA_NUMBER_OF_CHILDREN, 0)
        multiCityTripSearchModel.infant = data.getIntExtra(EXTRA_NUMBER_OF_INFANT, 0)
        multiCityTripSearchModel.classType = data.getStringExtra(EXTRA_TRIP_CLASS_TYPE).toString()

        if (multiCityTripSearchModel.child != 0)
            multiCityTripSearchModel.childDateOfBirthList =
                data.getParcelableArrayListExtra<ChildrenDOB>(EXTRA_CHILD_DOB_LIST) as ArrayList<ChildrenDOB>

        travelersCount.set("${multiCityTripSearchModel.adult + multiCityTripSearchModel.child + multiCityTripSearchModel.infant}")
        travelerType.set(if ((multiCityTripSearchModel.adult) > 1) "${multiCityTripSearchModel.adult} Adults" else "${multiCityTripSearchModel.adult} Adult")
        travelerClass.set("${multiCityTripSearchModel.classType}")

        travellers = TravellersInfo(
            multiCityTripSearchModel.classType,
            multiCityTripSearchModel.adult,
            multiCityTripSearchModel.child,
            multiCityTripSearchModel.infant,
            multiCityTripSearchModel.childDateOfBirthList,
            arrayListOf(),
            multiCityTripSearchModel.depart[0]
        )
    }

    fun handleFromAddress(data: Intent) {
        if (clickedPos in 0 until multiCityTripSearchModel.multiCityModels.size) {
            val mCode = data.getStringExtra(ARG_AIRPORT_CODE)
            val mCity = data.getStringExtra(ARG_AIRPORT_CITY)
            val mAddress = data.getStringExtra(ARG_AIRPORT_ADDRESS)
            val item = multiCityTripSearchModel.multiCityModels[clickedPos]
            item.origin = mCode!!
            item.originCity = mCity
            item.originAddress = mAddress
            changeItemAtPos.value = Pair(clickedPos, item)
            updateModelData()
        }
    }

    fun handleToAddress(data: Intent) {
        if (clickedPos in 0 until multiCityTripSearchModel.multiCityModels.size) {
            val mCode = data.getStringExtra(ARG_AIRPORT_CODE)
            val mCity = data.getStringExtra(ARG_AIRPORT_CITY)
            val mAddress = data.getStringExtra(ARG_AIRPORT_ADDRESS)
            val item = multiCityTripSearchModel.multiCityModels[clickedPos]
            item.destination = mCode!!
            item.destinationCity = mCity
            item.destinationAddress = mAddress
            changeItemAtPos.value = Pair(clickedPos, item)
            updateModelData()
            updateNextOriginOnDestinationChange(data)
        }
    }

    fun updateNextOriginOnDestinationChange(data: Intent) {
        if (clickedPos + 1 in 0 until multiCityTripSearchModel.multiCityModels.size) {
            val mCode = data.getStringExtra(ARG_AIRPORT_CODE)
            val mCity = data.getStringExtra(ARG_AIRPORT_CITY)
            val mAddress = data.getStringExtra(ARG_AIRPORT_ADDRESS)
            val item = multiCityTripSearchModel.multiCityModels[clickedPos + 1]
            item.origin = mCode!!
            item.originCity = mCity
            item.originAddress = mAddress
            changeItemAtPos.value = Pair(clickedPos + 1, item)
            updateModelData()
        }
    }

    fun handleDepartureDate(data: Long) {
        if (clickedPos in 0 until multiCityTripSearchModel.multiCityModels.size) {
            val mStartDate = DateUtil.parseApiDateFormatFromMillisecond(data)
            val item = multiCityTripSearchModel.multiCityModels[clickedPos]
            item.departDate = mStartDate
            changeItemAtPos.value = Pair(clickedPos, item)
            updateModelData()
        }
    }

    fun updateModelData() {
        val dataSet = multiCityTripSearchModel.multiCityModels

        val origin = multiCityTripSearchModel.origin
        origin.clear()

        val originCity = multiCityTripSearchModel.originCity
        originCity.clear()

        val originAddress = multiCityTripSearchModel.originAddress
        originAddress.clear()

        val destination = multiCityTripSearchModel.destination
        destination.clear()

        val destinationCity = multiCityTripSearchModel.destinationCity
        destinationCity.clear()

        val destinationAddress = multiCityTripSearchModel.destinationAddress
        destinationAddress.clear()

        val depart = multiCityTripSearchModel.depart
        depart.clear()

        for (model in dataSet) {
            origin.add(model.origin)
            originCity.add(model.originCity!!)
            originAddress.add(model.originAddress!!)
            destination.add(model.destination)
            destinationCity.add(model.destinationCity!!)
            destinationAddress.add(model.destinationAddress!!)
            depart.add(model.departDate)
        }
    }

    companion object {
        const val PLEASE_SELECT_ORIGIN_DESTINATION = "PLease select origin and destination airport."
        const val GOTO_TRAVELLER = "GOTO_TRAVELLER"
        const val PICK_DEPARTURE_DATE_REQUEST = "PICK_DEPARTURE_DATE_REQUEST"
        const val ARG_FLIGHT_LIST_DATA = "ARG_FLIGHT_LIST_DATA"
        const val GOTO_FLIGHT_LIST = "GOTO_FLIGHT_LIST"
        const val GOTO_TERMS = "GOTO_TERMS"
    }
}
