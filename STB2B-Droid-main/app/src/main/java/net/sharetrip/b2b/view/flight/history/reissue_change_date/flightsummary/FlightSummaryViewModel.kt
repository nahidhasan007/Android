package net.sharetrip.b2b.view.flight.history.reissue_change_date.flightsummary

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.RadioButton
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.util.DateFormatPattern
import net.sharetrip.b2b.util.DateUtil
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ErrorType
import net.sharetrip.b2b.widgets.BaseOperationalVm
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ApiCallingKey
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.COUPON
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.EARN
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.FlightCouponRequest
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.FlightExtraParams
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.FlightX
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.GPCouponInputState
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.PaymentGatewayType
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.PaymentMethod
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.PaymentOptionMsg
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.PriceBreakdown
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.REDEEM
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueBookingRequestBody
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueBookingResponse
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlightSearch
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.RemoteDiscountModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.PriceBreakDownUtil
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.*
import java.lang.Math.ceil
import java.text.ParseException
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.*

class FlightSummaryViewModel (
    val flightsInfo: FlightX,
    private val flightSearch: ReissueFlightSearch,
    private val sharedPrefsHelper: AppSharedPreference
) : BaseOperationalVm() {
    private val convenienceVatFlag = false
//    private val flightEventManager =
//        AnalyticsProvider.flightEventManager(AnalyticsProvider.getFirebaseAnalytics())
    private var priceBreakDownData: PriceBreakdown? = null
//    private var userTripCoin: Int = 0
    private lateinit var timer: CountDownTimer
    private lateinit var couponRequest: FlightCouponRequest

    val isCheckboxActive = ObservableBoolean(false)
    val paymentMethodList = MutableLiveData<List<PaymentMethod>>()
    val clickedBooking = MutableLiveData<Boolean>()
    private val redeemChecked = MutableLiveData<Boolean>()
    private val availCoupon = MutableLiveData<Boolean>()
    val cardPaymentChecked = MutableLiveData<Boolean>()
    val showMessageWithDialog = MutableLiveData<String>()
    val flightInfoLiveData = MutableLiveData<ArrayList<Any>>()
    val searchFlightAgain = MutableLiveData<String>()

    val usdPayment = MutableLiveData<Double>()
    val tripCoinText = MutableLiveData<Event<String>>()
    val isCardSelected = ObservableBoolean(false)
    val isRedeemSelected = ObservableBoolean(false)
    val isCouponSelected = ObservableBoolean(false)
    val isCouponShow = ObservableBoolean(true)
    val isFlightSummaryExpand = ObservableBoolean(false)
    val wannaRedeem = ObservableBoolean()
    val redeemCoin = ObservableInt()
    val progressBar = ObservableBoolean(false)
    val couponObserver = ObservableField<FlightCouponRequest>()
    val flightInfoObservable = ObservableField<String>()
    val flightDateObservable = ObservableField<String>()
    val currency = ObservableField<String>()
    val priceBreakdownMsg = ObservableField<CharSequence>()

    val payButtonText = ObservableField("Pay Now")
    var bankDiscountInfo = ""

    var flightCoupon = ""
    var conversionRate = 0.0
    var totalBaggageChargeBDT = 0.0
    var advanceIncomeTaxBDT = 0.0
    var bookingDate: Long? = null
    var returnBookingDate: Long? = null
    var isExtraDiscountVisible = ObservableBoolean(false)
    var extraDiscountLabel = ObservableField<String>()
    val extraDiscountAmount = ObservableInt()
    val isConvenienceVisible = ObservableBoolean(false)
    val selectedPaymentMethod = MutableLiveData<PaymentMethod>()
    val totalConvenienceCharge = ObservableField(0)
    val vatCharge = ObservableField(0)
    val totalPrice = ObservableInt()
    val totalPriceWithoutDiscount = ObservableInt()
    val discountAmount = ObservableInt()
    var discountAmountBDT = 0
    var covidAmount = ObservableInt(0)
    var covidAmountBDT = 0
    var travelInsuranceAmount = ObservableInt(0)
    var travelInsuranceAmountBDT = 0
    var baggageInsuranceAmount = ObservableInt(0)
    var baggageInsuranceAmountBDT = 0
    var priceCheckRes = MutableLiveData<Pair<Int, Int>>()
    val totalBaggageCharge = ObservableInt(0)
    val advanceIncomeTax = ObservableInt(0)
    val paymentOptionMsg: PaymentOptionMsg


    val couponState = MutableLiveData(GPCouponInputState.MobileInputState.name)
    val sendAgain = ObservableBoolean(false)
    val gpStarCouponVerificationNeeded = ObservableBoolean(false)
    var gpCouponTitle = ObservableField(couponTitle)
    var gpCouponSubTitle = ObservableField(couponSubTitle)
    var timerOrResendAction = ObservableField("")
    var gpCouponInputHint = ObservableField(enterPhone)
    var inputObserver = ObservableField<String>()
    var gpStarNumber = MutableLiveData<String>()
    var verifiedOTP = MutableLiveData<String>()

    private val mApiService by lazy {
        ServiceGenerator.createService(ReissueApiService::class.java)
    }

    init {
//        flightEventManager.initialCheckoutFlight()
//        userTripCoin = sharedPrefsHelper[USER_TRIP_COIN, "0"].filter { it in '0'..'9' }.toInt()

        val listType = object : TypeToken<List<RemoteDiscountModel>>() {}.type
        val flightDiscountModel = Gson().fromJson<List<RemoteDiscountModel>>(
            sharedPrefsHelper[FLIGHT_DISCOUNT_OPTIONS, DEFAULT_FLIGHT_DISCOUNT_OPTION],
            listType
        )

        totalBaggageCharge.set(ceil(flightSearch.totalBaggageCost).toInt())
        totalBaggageChargeBDT = flightSearch.totalBaggageCost

        paymentOptionMsg = PaymentOptionMsg(
            flightDiscountModel.find { it.type == EARN }!!.title,
            flightDiscountModel.find { it.type == REDEEM }!!.title,
            flightDiscountModel.find { it.type == COUPON }!!.title
        )

        bankDiscountInfo = sharedPrefsHelper[FLIGHT_DISCOUNT_OFFER_BANK_LIST, ""]
        wannaRedeem.set(false)
        couponObserver.set(FlightCouponRequest())

        setAdvanceIncomeTax()
        setFlightInfo()
        setFlightDate()
        initDetailsAdapter()
        fetchPaymentGateWay()
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when (operationTag) {

            ApiCallingKey.FetchPaymentGateWay.name -> {
                paymentMethodList.value =
                    (data.body as RestResponse<*>).response as List<PaymentMethod>
                dataLoading.set(false)
                paymentMethodSelection()
            }

            ApiCallingKey.FetchPaymentUrl.name -> {
                progressBar.set(false)
                val response = (data.body as RestResponse<*>).response as ReissueBookingResponse
                if (response.paymentRequired == true) {
                    val flightList = flightsInfo.segments
                    when (flightList.size) {
                        1 -> {
                            try {
                                bookingDate =
                                    DateUtil.parseDateTimeToMillisecond(
                                        flightList[0].departureDateTime.date,
                                        flightList[0].departureDateTime.time,
                                        dateFormat = DateFormatPattern.API_DATE_TIME_PATTERN.datePattern
                                    )
                            } catch (e: ParseException) {
                                e.printStackTrace()
                            }
                        }

                        2 -> {
                            try {
                                bookingDate =
                                    DateUtil.parseDateTimeToMillisecond(
                                        flightList[0].departureDateTime.date,
                                        flightList[0].departureDateTime.time,
                                        dateFormat = DateFormatPattern.API_DATE_TIME_PATTERN.datePattern
                                    )
                                returnBookingDate =
                                    DateUtil.parseDateTimeToMillisecond(
                                        flightList[flightList.size - 1].departureDateTime.date,
                                        flightList[flightList.size - 1].departureDateTime.time,
                                        dateFormat = DateFormatPattern.API_DATE_TIME_PATTERN.datePattern
                                    )
                            } catch (e: ParseException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    val bundle = Bundle()
                    val payment = response.payment
                    val parsableJsonString =
                        "{paymentUrl=${payment?.paymentUrl}, successUrl=${payment?.successUrl}, cancelUrl=${payment?.cancelUrl}, declineUrl=${payment?.declineUrl}}"
                    bundle.putString(ARG_PAYMENT_URL, parsableJsonString)
                    bundle.putString(SERVICE_TYPE, SERVICE_TYPE_FLIGHT)
                    navigateWithArgument(GOTO_PAYMENT, bundle)

                    sharedPrefsHelper.put(BOOKING_DATE, bookingDate ?: 0)
                    sharedPrefsHelper.put(RETURN_DATE, returnBookingDate ?: 0)
                }
            }
        }
    }

    private fun paymentMethodSelection() {
        cardPaymentChecked.value = true
    }

    private fun gpLoyaltyCheck(phoneNo: String) {
        progressBar.set(true)
        gpStarNumber.value = phoneNo
        val token = sharedPrefsHelper.accessToken
        executeSuspendedCodeBlock(ApiCallingKey.GpLoyaltyCheck.name) {
            mApiService.gpLoyaltyCheck(
                token,
                GpLoyaltyCheckRequest(mobileNumber = phoneNo)
            )
        }
    }

    private fun otpVerification(phoneNo: String, otp: String) {
        progressBar.set(true)
        verifiedOTP.value = otp
        val token = sharedPrefsHelper.accessToken
        executeSuspendedCodeBlock(ApiCallingKey.OTPVerify.name) {
            mApiService.verifyOTP(
                token,
                VerifyOTPRequest(mobileNumber = phoneNo, otp = otp)
            )
        }
    }

    override fun onAnyError(operationTag: String, errorMessage: String, type: ErrorType?) {
        when (operationTag) {
            ApiCallingKey.CouponRequest.name -> {
                progressBar.set(false)
                if (errorMessage.isNotEmpty() && errorMessage.length > 4)
                    showMessage(errorMessage)
                else
                    showMessage(SOMETHING_WENT_WRONG)
            }

            ApiCallingKey.CouponList.name -> {
                progressBar.set(false)
                if (errorMessage.isNotEmpty() && errorMessage.length > 4)
                    showMessage(errorMessage)
                else
                    showMessage(SOMETHING_WENT_WRONG)
            }

            ApiCallingKey.FetchPaymentGateWay.name -> {
                dataLoading.set(false)
                showMessage(errorMessage)
            }

            ApiCallingKey.FetchPaymentUrl.name -> {
                progressBar.set(false)
                if (errorMessage.isNotEmpty() && errorMessage.length > 4)
                    showMessage(errorMessage)
                else
                    showMessage(SOMETHING_WENT_WRONG)
            }

            ApiCallingKey.GpLoyaltyCheck.name -> {
                showMessage(errorMessage)
                progressBar.set(false)
            }

            ApiCallingKey.OTPVerify.name -> {
                showMessage(errorMessage)
                progressBar.set(false)
            }
        }
    }

    private fun setFlightInfo() {
//        val destination = flightSearch.multiCityModels[flightSearch.multiCityModels.size - 1].destination
//        val origin = flightSearch.multiCityModels[0].origin
//        flightInfoObservable.set("$origin - $destination")

        val destination =
            flightsInfo.segments[0].transits[flightsInfo.segments[0].transits.size - 1].destinationCode
        val origin = flightsInfo.segments[0].transits[0].originCode
        flightInfoObservable.set("$origin - $destination")
    }

    private fun setAdvanceIncomeTax() {
        flightsInfo.priceBreakdown.advanceIncomeTax?.let { advanceIncomeTax.set(ceil(it).toInt()) }
        flightsInfo.priceBreakdown.advanceIncomeTax?.let { advanceIncomeTaxBDT = it }
    }

    private fun initDetailsAdapter() {
        val mList = ArrayList<Any>()
        val mFlight = flightsInfo.segments
        val mItemSegments = flightsInfo.segments

        val mCount = mFlight.size
        for (i in 0 until mCount) {
            val flight = mItemSegments[i]
            mList.add(flight)
        }
        flightInfoLiveData.value = mList
    }


    private fun setFlightDate() {
        val mBuilder = StringBuilder()
        val depart = flightsInfo.segments[0].transits[0].departureDateTime.date
        if (flightsInfo.segments.size == 1) {
            try {
                val mDate = DateUtil.parseDisplayDateMonthFormatFromApiDateFormat(depart)
                mBuilder.append(mDate)
//                    .append(" " + flightSearch.numberOfTraveller.toString() + " Traveller(s)")
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        } else {
            try {
                val mStartDate = DateUtil.parseDisplayDateMonthFormatFromApiDateFormat(depart)
                val mEndDate =
                    DateUtil.parseDisplayDateMonthFormatFromApiDateFormat(flightsInfo.segments[0].transits[flightsInfo.segments[0].transits.size - 1].departureDateTime.date)
                mBuilder.append(mStartDate).append(" - ").append(mEndDate)
//                    .append(" " + flightSearch.numberOfTraveller.toString() + " Traveller(s)")
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        flightDateObservable.set(mBuilder.toString())
    }

    private fun onCouponRequest(couponReq: FlightCouponRequest) {
        progressBar.set(true)
        val token = sharedPrefsHelper.accessToken
        couponRequest = couponReq
        executeSuspendedCodeBlock(ApiCallingKey.CouponRequest.name) {
            mApiService.getValidateFlightCoupon(token, couponReq)
        }
    }

    private fun fetchPaymentGateWay(gateway: List<String>? = null) {
        dataLoading.set(true)
        executeSuspendedCodeBlock(ApiCallingKey.FetchPaymentGateWay.name) {
            mApiService.fetchPaymentGateway(
                GatewayService.FLIGHT_REISSUE.name,
                flightsInfo.gatewayCurrency,
                gateway
            )
        }
    }

    private fun getOriginPrice() = flightsInfo.priceBreakdown.originPrice

    private fun getOriginPriceForUSD() = priceBreakDownData!!.originPrice!!.twoDigitDecimal()

    private fun calculateTotalPrice() {
        flightsInfo.priceBreakdown.total?.let { updateFinalPayableWithConvenience(it) }
        setPayButtonText()
    }

    private fun updateFinalPayableWithConvenience(payableAmount: Double) {
        val finalPayableAmount =
            if (selectedPaymentMethod.value?.charge == null || selectedPaymentMethod.value?.charge == 0.0) {
                totalConvenienceCharge.set(0)
                vatCharge.set(0)
                isConvenienceVisible.set(false)
                payableAmount
            } else {
                val convenienceData = calculateConvenience(payableAmount)
                totalConvenienceCharge.set(ceil(convenienceData.first).toInt())
                vatCharge.set(ceil(convenienceData.second).toInt())
                isConvenienceVisible.set(true)
                convenienceData.third
            }

        totalPrice.set(ceil(finalPayableAmount).toInt())
    }

    private fun calculateConvenience(totalAmount: Double): Triple<Double, Double, Double> {
        val convenience = (selectedPaymentMethod.value?.charge!! / 100) * totalAmount
        val vat = if (convenienceVatFlag) {
            0.05 * convenience
        } else {
            0.0
        }

        return Triple(convenience, vat, totalAmount + convenience + vat)
    }

    private fun setPayButtonText() {
        if (totalPrice.get() == 0) {
            payButtonText.set("Book Now")
        } else {
            payButtonText.set("Pay Now")
        }
    }

    private fun counter(seconds: Long = COUNTDOWN_TIME) {
        sendAgain.set(false)
        timer = object : CountDownTimer(seconds, COUNTDOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                val min = (millisUntilFinished / 1000) / 60
                val sec = (millisUntilFinished / 1000) % 60
                timerOrResendAction.set(String.format(COUNTDOWN_FORMAT, min, sec))
            }

            override fun onFinish() {
                sendAgain.set(true)
                timerOrResendAction.set(otpResend)
            }
        }
        timer.start()
    }

    fun onCouponApply() {
        val couponRequest = couponObserver.get()
        couponRequest?.let {
            it.deviceType = "Android"
            it.serviceType = "Flight"
            it.extraParams = FlightExtraParams(flightSearch.searchId, flightSearch.sequence)
            onCouponRequest(it)
        }
    }

    fun onVerifyClick() {
        if (couponState.value != GPCouponInputState.OTPInputState.name) {
            if (inputObserver.get().isPhoneNumberValid()) {
                gpLoyaltyCheck(inputObserver.get().toString())
            } else {
                showMessage(PLEASE_PROVIDE_A_VALID_PHONE_NUMBER)
            }

        } else {
            otpVerification(gpStarNumber.value.toString(), inputObserver.get().toString())
        }
    }

    fun onResendClick() {
        if (timerOrResendAction.get() == otpResend) {
            gpLoyaltyCheck(gpStarNumber.value.toString())
        }
    }

    fun useAnotherNumber() {
        gpCouponInputHint.set(enterPhone)
        couponState.value = GPCouponInputState.MobileInputState.name
        if (::timer.isInitialized) timer.cancel()
        timerOrResendAction.set("")
        inputObserver.set("")
    }

    fun onClickCheckBox(view: View) {
        if (view is RadioButton) {
            /*
        todo: repaoir it later
            when (view.id) {
                R.id.redeem_check_box ->
                    redeemChecked.value = true
                R.id.radio_button_card_payment ->
                    cardPaymentChecked.value = true
                R.id.radio_button_coupon ->
                    availCoupon.value = true
            }
            */
        }
    }

    fun onClickBookNow() {
        if (isCouponSelected.get() && flightCoupon.isEmpty()) {
            showMessage("Please apply a coupon or select another option")
        } else if (isCouponSelected.get() && gpStarCouponVerificationNeeded.get()) {
            showMessage("Please verify your GPSTAR number")
        } else {
            if (isCheckboxActive.get()) {
                clickedBooking.value = true
            } else {
                showMessage(CLICK_TO_AGREE)
            }
        }
    }

    fun fetchPaymentUrl(bookingDetail: ReissueBookingRequestBody) {
        progressBar.set(true)
        val token = sharedPrefsHelper.accessToken
        executeSuspendedCodeBlock(ApiCallingKey.FetchPaymentUrl.name) {
            mApiService.reissueFlightBooking(token, bookingDetail)
        }
    }

    fun onSummaryLayoutClick() {
        isFlightSummaryExpand.set(!isFlightSummaryExpand.get())
    }

    fun goToFlightDashboard() {
        navigateWithArgument(GOTO_FLIGHT_DASHBOARD, "")
    }

    fun onClickTermsAndConditionCheckbox() {
        isCheckboxActive.set(!isCheckboxActive.get())
    }

    fun onClickPrivacyPolicy() {
        navigateWithArgument(GOTO_PRIVACY, "")
    }

    fun onClickTermsAndCondition() {
        navigateWithArgument(GOTO_TERMS, "")
    }

    fun updateUSDPayment(paymentMethod: PaymentMethod) {
        if (priceBreakDownData == null) {
            conversionRate = paymentMethod.currency.conversion.rate
            priceBreakDownData = PriceBreakdown(details = ArrayList())

            priceBreakDownData?.apply {
                flightsInfo.priceBreakdown.total?.let { total = it }
                flightsInfo.priceBreakdown.couponAmount?.let { couponAmount = it }
                flightsInfo.priceBreakdown.discountAmount?.let { discountAmount = it }
                flightsInfo.priceBreakdown.total?.let { total = it }

                originPrice = getOriginPrice()!! / conversionRate
                promotionalAmount = flightsInfo.priceBreakdown.promotionalAmount!! / conversionRate

                promotionalDiscount = flightsInfo.priceBreakdown.promotionalDiscount
                currency = flightsInfo.priceBreakdown.currency
            }

            flightsInfo.priceBreakdown.details?.forEach {
                (priceBreakDownData?.details as ArrayList).add(it.copy())
            }

            priceBreakDownData?.details?.forEachIndexed { index, priceDetail ->
                val base = (priceDetail.baseFare.toDouble() / conversionRate).toString()
                priceBreakDownData!!.details!![index].baseFare = base.toDouble()
                priceBreakDownData!!.details!![index].tax =
                    (priceDetail.tax.toDouble() / conversionRate).toDouble()
            }
        }

        currency.set(PaymentGatewayType.USD.toString())
        val priceString =
            PriceBreakDownUtil.getFormattedPriceBreakDownForReissue(priceBreakDownData!!)
        priceBreakdownMsg.set(priceString)
        currency.notifyChange()
        priceBreakdownMsg.notifyChange()

        val covidTestPrice = covidAmount.get()
        if (covidTestPrice != 0) {
            val charge = covidTestPrice / conversionRate
            covidAmount.set(ceil(charge).toInt())
        }

        val travelInsurancePrice = travelInsuranceAmount.get()
        if (travelInsurancePrice != 0) {
            val charge = covidTestPrice / conversionRate
            travelInsuranceAmount.set(ceil(charge).toInt())
        }

        val aitCharge = advanceIncomeTax.get()
        if (aitCharge != 0) {
            val amount = aitCharge / conversionRate
            advanceIncomeTax.set(ceil(amount).toInt())
        }

        val baggageCharge = totalBaggageCharge.get()
        if (baggageCharge != 0) {
            val charge = baggageCharge / conversionRate
            totalBaggageCharge.set(ceil(charge).toInt())
        } else {
            totalBaggageCharge.set(baggageCharge)
        }
        calculateTotalPrice()
    }

    fun updateWithBDTPayment() {
        if (currency.get() == PaymentGatewayType.USD.toString()) {
            advanceIncomeTax.set(ceil(advanceIncomeTaxBDT).toInt())
            covidAmount.set(covidAmountBDT)
            travelInsuranceAmount.set(travelInsuranceAmountBDT)
            baggageInsuranceAmount.set(baggageInsuranceAmountBDT)
            totalBaggageCharge.set(ceil(advanceIncomeTaxBDT).toInt())
            discountAmount.set(discountAmountBDT)
        }
        currency.set(PaymentGatewayType.BDT.toString())
        calculateTotalPrice()
        priceBreakdownMsg.set(PriceBreakDownUtil.getFormattedPriceBreakDownForReissue(flightsInfo.priceBreakdown))
        currency.notifyChange()
        priceBreakdownMsg.notifyChange()
    }

    companion object {
        const val GOTO_PAYMENT = "GOTO_PAYMENT"
        const val GOTO_TERMS = "GOTO_TERMS"
        const val GOTO_PRIVACY = "GOTO_PRIVACY"
        const val GOTO_FLIGHT_DASHBOARD = "GOTO_FLIGHT_DASHBOARD"
        const val BOOK_REISSUE = "book_reissue"
    }
}