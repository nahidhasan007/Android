package net.sharetrip.b2b.view.flight.booking.ui.summary

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.EmptyResponse
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.*
import net.sharetrip.b2b.util.MsgUtils.unKnownErrorMsg
import net.sharetrip.b2b.view.flight.booking.model.*
import net.sharetrip.b2b.view.topup.model.AgencyBalance

class BookingSummaryVM(
    private val bookingSummaryRepo: BookingSummaryRepo,
    checkBalance: Boolean,
    private val contactInfo: ContactInfo
) :
    ViewModel() {
    private lateinit var passengers: List<Passenger>
    private lateinit var flightDetails: FlightSearch

    val flightSearch = MutableLiveData<FlightSearch>()
    val dataLoading = ObservableBoolean(false)
    val instantPurchase = ObservableBoolean(false)
    val isConfirmed = MutableLiveData<Event<Boolean>>()
    var moveToConfirmation = MutableLiveData<Event<Boolean>>()
    val showMessage = MutableLiveData<Event<String>>()
    val bookingButtonStatus = MutableLiveData<Pair<String, Boolean>>()

    init {
        viewModelScope.launch {
            flightDetails = bookingSummaryRepo.getFlightDetails()
            if (::flightDetails.isInitialized) {
                flightSearch.value = flightDetails
                if (checkBalance) {
                    flightDetails.flights?.instantPurchase?.let { instantPurchase.set(it) }
                    checkBalance(false)
                }
            } else {
                showMessage.value = Event(unKnownErrorMsg)
            }
            passengers = bookingSummaryRepo.getPassengerList()
        }
    }

    fun checkBalance(isBookTicket: Boolean) {
        viewModelScope.launch {
            dataLoading.set(true)
            val data = bookingSummaryRepo.checkBalance()
            dataLoading.set(false)
            balanceResponseOperation(data, isBookTicket)
        }
    }

    private fun bookFlight(bookButtonPair: Pair<String, Boolean>?) {
        viewModelScope.launch {
            if (!passengers.isNullOrEmpty()) {
                if (bookingButtonStatus.value?.second!!) {
                    val passenger = Passengers().apply {
                        adult = passengers.filter { it.id.contains("Adult") }
                        child = passengers.filter { it.id.contains("Child") }
                        infant = passengers.filter { it.id.contains("Infant") }
                    }
                    if (::flightDetails.isInitialized) {
                        dataLoading.set(true)
                        if (bookButtonPair != null) {
                            bookingButtonStatus.value = Pair(bookButtonPair.first, false)
                        } else {
                            bookingButtonStatus.value = Pair(BOOK_TICKETS, false)
                        }
                        val data = bookingSummaryRepo.bookFlight(
                            BookingDetails(
                                flightDetails.searchId,
                                flightDetails.flights!!.sequence,
                                flightDetails.sessionId,
                                passenger,
                                contactInfo,
                                "",
                                ""
                            )
                        )
                        if (bookButtonPair != null) {
                            bookingButtonStatus.value =
                                Pair(bookButtonPair.first, bookButtonPair.second)
                        } else {
                            bookingButtonStatus.value = Pair(BOOK_TICKETS, true)
                        }
                        dataLoading.set(false)
                        bookingResponseOperation(data)
                    } else {
                        showMessage.value = Event(unKnownErrorMsg)
                    }
                } else {
                    showMessage.value = Event(INSUFFICIENT_BALANCE)
                }

            }
        }
    }

    private fun bookingResponseOperation(data: GenericResponse<RestResponse<EmptyResponse>>) {
        when (data) {
            is BaseResponse.Success -> {
                isConfirmed.value = Event(true)
                moveToConfirmation.value = Event(true)
            }

            is BaseResponse.ApiError -> showMessage.value = Event(data.errorBody.message)

            is BaseResponse.NetworkError ->
                showMessage.value = Event(MsgUtils.networkErrorMsg)

            is BaseResponse.UnknownError ->
                showMessage.value = Event(MsgUtils.unKnownErrorMsg)
        }
    }

    private fun balanceResponseOperation(
        data: GenericResponse<RestResponse<AgencyBalance>>,
        isBookTicket: Boolean
    ) {
        var bookButtonPair: Pair<String, Boolean>? = null
        when (data) {
            is BaseResponse.Success -> {
                val hasBalance =
                    flightDetails.flights?.originPrice?.let { it <= data.body.response.balance }

                if (!flightDetails.flights?.instantPurchase!!) {
                    bookButtonPair = Pair(BOOK_TICKETS, true)
                } else if (hasBalance!! && flightDetails.flights?.instantPurchase!!) {
                    bookButtonPair = Pair(ISSUE_TICKETS, true)
                } else if (!hasBalance && flightDetails.flights?.instantPurchase!!) {
                    bookButtonPair = Pair(ISSUE_TICKETS, false)
                }
                if (bookButtonPair != null) {
                    bookingButtonStatus.value = bookButtonPair
                }
                if (isBookTicket) {
                    bookFlight(bookButtonPair)
                }
            }

            is BaseResponse.ApiError -> showMessage.value = Event(data.errorBody.message)

            is BaseResponse.NetworkError -> showMessage.value = Event(MsgUtils.networkErrorMsg)

            is BaseResponse.UnknownError -> showMessage.value = Event(MsgUtils.unKnownErrorMsg)
        }
    }
}
