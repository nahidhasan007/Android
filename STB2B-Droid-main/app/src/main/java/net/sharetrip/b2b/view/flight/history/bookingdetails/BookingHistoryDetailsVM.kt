package net.sharetrip.b2b.view.flight.history.bookingdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.view.flight.history.model.FlightHistory
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ErrorType
import net.sharetrip.b2b.widgets.BaseOperationalVm

class BookingHistoryDetailsVM(
    val historyResponse: FlightHistory,
//    private var token: String,
//    private val apiService: FlightHistoryApiService
) : BaseOperationalVm() {
//    var isLoaderVisible = MutableLiveData<Boolean>()
//    var bookingDate: Long? = null
//    var returnBookingDate: Long? = null
//    val goBack = MutableLiveData<Event<Boolean>>()
//    val gotoVoidConfirmation = MutableLiveData<Event<VoidQuotationResponse>>()
//
//    private val _reissueEligibilityResponse = MutableLiveData<Event<ReissueEligibilityResponse>>()
//    val reissueEligibilityResponse: LiveData<Event<ReissueEligibilityResponse>>
//        get() = _reissueEligibilityResponse
//
//    val showToast: MutableLiveData<String> by lazy {
//        MutableLiveData<String>()
//    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when (operationTag) {
//            ApiCallingKey.CancelFlight.name -> {
//                isLoaderVisible.value = false
//                showToast.value = YOUR_BOOKING_HAS_BEEN_SUCCESSFULLY_CANCELLED
//                goBack.value = Event(true)
//            }
//
//            ApiCallingKey.ResendVoucher.name -> {
//                isLoaderVisible.value = false
//                showToast.value = YOUR_VOUCHER_REQUEST_HAS_BEEN_SUCCESSFULLY_SENT
//            }
//
//            ApiCallingKey.RetryPayment.name -> {
//                isLoaderVisible.value = false
//                val flightList = historyResponse.flight
//                when (flightList.size) {
//                    1 -> {
//                        try {
//                            bookingDate =
//                                DateUtil.parseDateTimeToMillisecond(
//                                    flightList[0].departureDateTime?.date,
//                                    flightList[0].departureDateTime?.time
//                                )
//                        } catch (e: ParseException) {
//                            e.printStackTrace()
//                        }
//                    }
//                    2 -> {
//                        try {
//                            bookingDate =
//                                DateUtil.parseDateTimeToMillisecond(
//                                    flightList[0].departureDateTime?.date,
//                                    flightList[0].departureDateTime?.time
//                                )
//                            returnBookingDate =
//                                DateUtil.parseDateTimeToMillisecond(
//                                    flightList[flightList.size - 1].departureDateTime?.date,
//                                    flightList[flightList.size - 1].departureDateTime?.time
//                                )
//                        } catch (e: ParseException) {
//                            e.printStackTrace()
//                        }
//                    }
//                }
//
//                val bundle = Bundle()
//                bundle.putString(ARG_PAYMENT_URL, data.body.toString())
//                bundle.putString(SERVICE_TYPE, SERVICE_TYPE_FLIGHT)
//                navigateWithArgument(GOTO_PAYMENT, bundle)
//            }
//
//            ApiCallingKey.VoidQuotation.name -> {
//                isLoaderVisible.value = false
//                gotoVoidConfirmation.postValue(Event((data.body as RestResponse<*>).response as VoidQuotationResponse))
//            }
//
//            ApiCallingKey.ReissueEligibilityCheck.name -> {
//                val response = (data.body as RestResponse<*>).response as ReissueEligibilityResponse
//                _reissueEligibilityResponse.postValue(Event(response))
//            }
//            ApiCallingKey.ReissueRetryPayment.name -> {
//                val payment = (data.body as RestResponse<*>).response as Payment
//                val bundle = Bundle()
//                val parsableJsonString = "{paymentUrl=${payment?.paymentUrl}, successUrl=${payment?.successUrl}, cancelUrl=${payment?.cancelUrl}, declineUrl=${payment?.declineUrl}}"
//                bundle.putString(ARG_PAYMENT_URL, parsableJsonString)
//                bundle.putString(SERVICE_TYPE, SERVICE_TYPE_FLIGHT)
//                navigateWithArgument(GOTO_PAYMENT, bundle)
//
//            }
//        }
        }

    }


        override fun onAnyError(operationTag: String, errorMessage: String, type: ErrorType?) {
            when (operationTag) {
//            ApiCallingKey.CancelFlight.name -> {
//                isLoaderVisible.value = false
//                showToast.value = errorMessage
//            }
//
//            ApiCallingKey.ResendVoucher.name -> {
//                isLoaderVisible.value = false
//                showToast.value = errorMessage
//            }
//
//            ApiCallingKey.RetryPayment.name -> {
//                isLoaderVisible.value = false
//                showToast.value = errorMessage
//            }
//
//            ApiCallingKey.VoidQuotation.name -> {
//                isLoaderVisible.value = false
//                showMessage(errorMessage)
//            }
//            ApiCallingKey.ReissueEligibilityCheck.name -> {
//                isLoaderVisible.value = false
//                showMessage(errorMessage)
//            }
            }
        }


//        fun onClickRetryPayment(reissueCode: String) {
//            val retryPaymentBody = RetryPaymentBody(reissueCode)
//            executeSuspendedCodeBlock(ApiCallingKey.ReissueRetryPayment.name) {
//                apiService.reissueRetryPayment(token, retryPaymentBody)
//            }
//        }

        companion object {
            const val GOTO_PAYMENT = "GOTO_PAYMENT"
        }
    }