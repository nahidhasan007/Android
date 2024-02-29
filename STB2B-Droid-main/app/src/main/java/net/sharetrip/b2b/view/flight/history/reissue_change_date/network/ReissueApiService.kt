package net.sharetrip.b2b.view.flight.history.reissue_change_date.network

import net.sharetrip.b2b.model.EmptyResponse
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.view.flight.booking.model.Airport
import net.sharetrip.b2b.view.flight.history.model.PriceBreakdown
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.*
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.VerifyOTPRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ReissueApiService {
    @POST("/api/v1/loyalty-check")
    suspend fun gpLoyaltyCheck(
        @Header("accesstoken") key: String,
        @Body gpLoyaltyCheckRequest: GpLoyaltyCheckRequest
    ): GenericResponse<RestResponse<GPLoyaltyCheckResponse>>


    @POST("api/v1/otp-verify")
    suspend fun verifyOTP(
        @Header("accesstoken") key: String,
        @Body verifyOTPRequest: VerifyOTPRequest
    ): GenericResponse<RestResponse<EmptyResponse>>

    @POST("api/v1/flight/reissue/book")
    suspend fun reissueFlightBooking(
        @Header("accesstoken") key: String,
        @Body reissueBookingRequestBody: ReissueBookingRequestBody
    ): GenericResponse<RestResponse<ReissueBookingResponse>>


    @POST("/api/v1/coupon/validate")
    suspend fun getValidateFlightCoupon(
        @Header("accesstoken") key: String,
        @Body couponRequest: FlightCouponRequest
    ): GenericResponse<RestResponse<CouponResponse>>

    @GET("api/v1/payment/gateWay")
    suspend fun fetchPaymentGateway(
        @Query("service") service: String,
        @Query("currency") currency: String = "ALL", @Query("bankCode") code: List<String>?
    ): GenericResponse<RestResponse<List<PaymentMethod>>>

    // todo: verify the apis
    @GET("api/v1/flight/reissue/eligibility")
    suspend fun checkReissueEligibility(
        @Header("accesstoken") key: String,
        @Query("bookingCode") bookingCode: String,
    ): GenericResponse<RestResponse<ReissueEligibilityResponse>>

    @POST("flight/reissue/quotation")
    suspend fun reissueRequestQuotation(
        @Header("accesstoken") key: String,
        @Body reissueQuotationRequestBody: ReissueQuotationRequestBody
    ): GenericResponse<RestResponse<ReissueFlightSearchResponse>>

    @POST("flight/reissue/quotation-filter")
    suspend fun reissueRequestQuotationFilter(
        @Header("accesstoken") key: String,
        @Body quotationFilterBody: ReissueQuotationFilterBody
    ): GenericResponse<RestResponse<ReissueQuotationFilterResponse>>

    @POST("flight/reissue/quotation-cancel")
    suspend fun cancelReissueManaualRequest(
        @Header("accesstoken") key: String,
        @Body reissueManualCancelBody: ReissueManualCancelBody
    ): GenericResponse<RestResponse<ReissueManualCancelResponse>>

    @POST("flight/reissue/book")
    suspend fun confirmReissue(
        @Header("accesstoken") key: String,
        @Body reissueManualConfirm: ConfirmReissueBody
    ): GenericResponse<RestResponse<Any>>

    @GET("flight/reissue/download-voucher")
    suspend fun downloadVoucher(
        @Header("accesstoken") key: String,
        @Query("reissueCode") reissueCode: String,
        @Query("hidePrice") hidePrice: Int,
        @Query("attachCompany") attachCompany: Int,
        @Query("applyCustomPricing") applyCustomPricing: Int,
        @Query("customPricing") customPricing: String
    ): GenericResponse<RestResponse<Any>>

    @GET("flight/advance-search")
    suspend fun getFlightCalendarPriceInfo(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("depart") departOne: String?,
        @Query("depart") departTwo: String?
    ): GenericResponse<RestResponse<AdvanceSearchResponse>>

    @GET("api/v1/flight/search/airport")
    suspend fun getAirports(@Query("name") mSearchText: String): GenericResponse<RestResponse<List<Airport>>>

    @POST("flight/reissue/quotation-select")
    suspend fun reissueRequestQuotationSelect(
        @Header("accesstoken") key: String,
        @Body reissueQuotationRequestSelectBody: ReissueQuotationRequestSelectBody
    ): GenericResponse<RestResponse<ReissueQuotationRequestSelectResponse>>

}

/*
 I  --> POST https://stg-b2b.api.sharetrip.net/api/v1/flight/reissue/quotation
 Content-Type: application/json; charset=UTF-8
 Content-Length: 161
 accesstoken: $2y$13$cq6CBZxyws4Hx3f.eTHs2.ytgMv4SgU/XnTWnutzHjvLjruO4A6MW
 userToken: $2y$13$cq6CBZxyws4Hx3f.eTHs2.ytgMv4SgU/XnTWnutzHjvLjruO4A6MW
 st-access: 7C8ABC7DFB20B0D4528E532DA4DA664C7028E4AF
 {"bookingCode":"STFL16944111333850","cabinClass":"Economy","flights":[{"date":"2023-09-27","destination":"SIN","origin":"DAC"}],"isNowShow":true,"travellers":[]}
 --> END POST (161-byte body)
 <-- 400 Bad Request https://stg-b2b.api.sharetrip.net/api/v1/flight/reissue/quotation (148ms)
 Date: Tue, 12 Sep 2023 04:12:01 GMT
 Content-Type: application/json; charset=utf-8
 Content-Length: 168
 Connection: keep-alive
 X-Powered-By: Sails <sailsjs.com>
 Access-Control-Allow-Origin:


etrip.b2b                    W  A resource failed to call close.
 --> GET https://stg-b2b.api.sharetrip.net/api/v1/flight/reissue/eligibility?bookingCode=STFL16944111333850
 accesstoken: $2y$13$cq6CBZxyws4Hx3f.eTHs2.ytgMv4SgU/XnTWnutzHjvLjruO4A6MW
 userToken: $2y$13$cq6CBZxyws4Hx3f.eTHs2.ytgMv4SgU/XnTWnutzHjvLjruO4A6MW
 st-access: 7C8ABC7DFB20B0D4528E532DA4DA664C7028E4AF
 --> END GET
 <-- 200 OK https://stg-b2b.api.sharetrip.net/api/v1/flight/reissue/eligibility?bookingCode=STFL16944111333850 (131ms)
 Date: Tue, 12 Sep 2023 04:18:28 GMT
 Content-Type: application/json; charset=utf-8
 Transfer-Encoding: chunked
 Connection: keep-alive
 X-Powered-By: Sails <sailsjs.com>
 Access-Control-Allow-Origin: *
 X-Exit: success
 ETag: W/"538-KcGlNIfteRjoMFwmxncuVMBItw8"
 Vary: Accept-Encoding
 set-cookie: sails.sid=s%3AVkZ5AF22oqC6cvPfrmBXyu9ohDaS3JsO.wB7IhdTOvwnVPTbkevm0C9VoHnd2pm1WPMb9Dowhm18; Path=/; Expires=Wed, 13 Sep 2023 04:18:28 GMT; HttpOnly
 {"code":"SUCCESS","message":"Success","response":{"automationSupported":true,"isNoShowReissue":false,"isNoShowPeriodOver":false,"isPartiallyFlown":false,"isNoShowReissueable":false,"paxSelectionType":"MULTIPLE","paxSelectionMsg":"You can select more than one traveller at once. Reissuing this ticket will affect all the selected travellers according to the airline's policy.","selectFlightMsg":null,"isJourneyChange":true,"isFullJourneySelect":false,"cabinClass":"Economy","cabinClassUpgradeable":false,"pendingModifications":[],"isPartiallyPaid":false,"skipSelection":false,"reissueSearch":null,"bookingCode":"STFL16944111333850","travellers":[{"id":"9979342470647","eTicket":"9979342470647","titleName":"MR","givenName":"JOHN","surName":"DOE","dateOfBirth":"2000-09-30T00:00:00.000Z","travellerType":"Adult","hash":"a571f0065d9e8df16b5184e7e6806958","paxNumber":"1.1","paxAssociated":null,"hasPendingModification":false}],"flights":[{"id":"7945c5cbdedc25fffd86afeca7c4fc21","originCode":"DAC","destinationCode":"SIN","origin":{"code":"DAC","city":"Dhaka","country":"Bangladesh","airport":"Hazrat Shahjalal International Airport","terminal":null},"destination":{"code":"SIN","city":"Singapore","country":"Singapore","airport":"Singapore Changi Airport","terminal":null},"departure":{"dateTime":"2023-09-30 08:30:00","timezone":"6"}}]}}
 <-- END HTTP (1336-byte body)
 Storing event with priority=DEFAULT, name=FIREPERF for destination cct
 Upload for context TransportContext(cct, DEFAULT, MSRodHRwczovL2ZpcmViYXNlbG9nZ2luZy1wYS5nb29nbGVhcGlzLmNvbS92MS9maXJlbG9nL2xlZ2FjeS9iYXRjaGxvZ1xBSXphU3lDY2traUg4aTJaQVJ3T3MxTEV6RktsZDE1YU9HOG96S28=) is already scheduled. Returning...
 flightHistory: ReissueEligibilityResponse(automationSupported=true, isNoShowReissue=false, isNoShowPeriodOver=false, isPartiallyFlown=false, isNoShowReissueable=false, bookingCode=STFL16944111333850, cabinClass=Economy, cabinClassUpgradeable=false, flights=[ReissueFlight(id=7945c5cbdedc25fffd86afeca7c4fc21, originCode=DAC, destinationCode=SIN, origin=TravelLocation(code=DAC, city=Dhaka, country=Bangladesh, airport=Hazrat Shahjalal International Airport, terminal=null), destination=TravelLocation(code=SIN, city=Singapore, country=Singapore, airport=Singapore Changi Airport, terminal=null), departure=Departure(dateTime=2023-09-30 08:30:00, timeZone=null))], isFullJourneySelect=false, isJourneyChange=true, paxSelectionMsg=You can select more than one traveller at once. Reissuing this ticket will affect all the selected travellers according to the airline's policy., paxSelectionType=MULTIPLE, reissueSearch=null, selectFlightMsg=null, skipSelection=false, travellers=[Traveller(id=null, titleName=MR, firstName=JOHN, lastName=DOE, gender=null, nationality=null, dateOfBirth=2000-09-30T00:00:00.000Z, passportNumber=null, frequentFlyerNumber=null, passportExpireDate=null, seatPreference=null, mealPreference=null, wheelChair=null, passportCopy=null, visaCopy=null, eTicket=9979342470647, travellerType=Adult, email=null, mobileNumber=null, isChecked=false, primaryContact=null)], pendingModifications=[], isPartiallyPaid=false)
 Compiler allocated 4533KB to com
 *


 {"code":"SUCCESS","message":"Success","response":{
    "automationSupported":true,
    "isNoShowReissue":false,
    "isNoShowPeriodOver":false,
    "isPartiallyFlown":false,
    "isNoShowReissueable":false,
    "paxSelectionType":"MULTIPLE",
    "paxSelectionMsg":"You can select more than one traveller at once. Reissuing this ticket will affect all the selected travellers according to the airline's policy.",
    "selectFlightMsg":null,
    "isJourneyChange":true,
    "isFullJourneySelect":false,
    "cabinClass":"Economy",
    "cabinClassUpgradeable":false,
    "pendingModifications":[],
    "isPartiallyPaid":false,
    "skipSelection":false,
    "reissueSearch":null,
    "bookingCode":"STFL16944111333850",
    "travellers":[{
        "id":"9979342470647",
        "eTicket":"9979342470647",
        "titleName":"MR",
        "givenName":"JOHN",
        "surName":"DOE",
        "dateOfBirth":"2000-09-30T00:00:00.000Z",
        "travellerType":"Adult",
        "hash":"a571f0065d9e8df16b5184e7e6806958",
        "paxNumber":"1.1",
        "paxAssociated":null,
        "hasPendingModification":false
    }],
    "flights":[{
        "id":"7945c5cbdedc25fffd86afeca7c4fc21",
        "originCode":"DAC",
        "destinationCode":"SIN",
        "origin":{"code":"DAC","city":"Dhaka","country":"Bangladesh","airport":"Hazrat Shahjalal International Airport","terminal":null},
        "destination":{"code":"SIN","city":"Singapore","country":"Singapore","airport":"Singapore Changi Airport","terminal":null},
        "departure":{"dateTime":"2023-09-30 08:30:00","timezone":"6"}}]}}
 */

