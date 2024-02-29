package net.sharetrip.b2b.network

import net.sharetrip.b2b.model.EmptyResponse
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.view.flight.booking.model.*
import net.sharetrip.b2b.view.flight.booking.model.BookingDetails
import net.sharetrip.b2b.view.flight.history.model.*
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.VRRConfirmResponse
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.RefundSearchIdBody
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueEligibilityResponse
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.VoidSearchIdBody
import okhttp3.MultipartBody
import retrofit2.http.*

interface FlightEndPoint {

    @GET("airport")
    suspend fun fetchAirportList(
        @Query("name") keyword: String,
        @Query("page") page: Int = 1
    ): GenericResponse<RestResponse<List<Airport>>>

    @GET("flight/search")
    suspend fun searchFlight(
        @Query("tripType") tripType: String,
        @Query("adult") adult: Int,
        @Query("child") child: Int,
        @Query("infant") infant: Int,
        @Query("class") classType: String,
        @Query("origin") origin: List<String>,
        @Query("destination") destination: List<String>,
        @Query("depart") depart: List<String>,
        @Query("childAge") childAge : ArrayList<String>? = null
    ): GenericRestResponse<FlightSearchResponse>

    @POST("flight/search/filter")
    suspend fun flightFilter(
        @Body filter: FilterData
    ): GenericResponse<RestResponse<FlightSearchResponse>>

    @GET("flight/search/fare-rules")
    suspend fun getFlightRules(
        @Query("searchId") searchId: String,
        @Query("sessionId") sessionId: String,
        @Query("sequenceCode") sequenceCode: String
    ): GenericResponse<RestResponse<AirFareResponse>>

    @GET("country")
    suspend fun getCountryList(): GenericResponse<RestResponse<List<Nationality>>>

    @Headers("isAccessToken: true")
    @POST
    suspend fun sendMail(
        @Url url: String,
        @Body flightDetails: FlightDetails
    ): GenericResponse<RestResponse<EmptyResponse>>

    @Headers("isAccessToken: true")
    @POST
    suspend fun downloadProposal(
        @Url url: String,
        @Body flightDetails: FlightDetails
    ): GenericResponse<RestResponse<ProposalDownloadResponse>>

    @POST("flight/booking")
    suspend fun flightBooking(
        @Body bookingDetails: BookingDetails
    ): GenericResponse<RestResponse<EmptyResponse>>

    @POST("flight/booking")
    suspend fun issueTicketFromHistory(
        @Body bookingDetails: BookingDetailsFromHistory
    ): GenericResponse<RestResponse<EmptyResponse>>

    @Multipart
    @POST("upload-file")
    suspend fun sendFile(
        @Part uploadFile: MultipartBody.Part,
        @Part("serviceTag") serviceTag: String
    ): GenericResponse<RestResponse<ImageUploadResponse>>

    @GET("flight/booking/history")
    suspend fun getFlightHistory(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("status") status: String? = null,
    ): GenericResponse<RestResponse<FlightHistoryResponse>>

    @GET("flight/booking/exchange-request-check")
    suspend fun getFlightHistoryButton(
        @Query("bookingCode") bookingCode: String,
        @Query("pnrCode") pnrCode: String,
    ): GenericResponse<RestResponse<FlightHistoryActionResponse>>

    @GET("flight/issue")
    suspend fun issueTicket(
        @Query("bookingCode") bookingCode: String,
        @Query("pnrCode") pnrCode: String,
    ): GenericResponse<RestResponse<IssueTicketResponse>>

    @GET("flight/booking/cancel")
    suspend fun cancelBooking(
        @Query("bookingCode") bookingCode: String,
    ): GenericResponse<RestResponse<EmptyResponse>>

    @GET("flight/booking/history")
    suspend fun changeTicket(
        @Query("limit") limit: Int = 10
    ): GenericResponse<RestResponse<ChangeTicketResponse>>

    @POST("flight/booking/{actionName}")
    suspend fun modifyTicket(
        @Body voidDetails: VoidDetails,
        @Path("actionName") actionName: String
    ): GenericResponse<RestResponse<VoidResponse>>

    @GET("flight/booking/exchange-request")
    suspend fun exchangeRequest(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 10,
        @Query("filter", encoded = true) filter: String? = null
    ): GenericResponse<RestResponse<ChangeTicketListResponse>>

    @GET("flight/booking/exchange-request-detail")
    suspend fun exchangeRequestDetails(
        @Query("uuid") uuid: String
    ): GenericResponse<RestResponse<ChangeTicketDetails>>

    @PATCH("flight/booking/exchange-request-update")
    suspend fun acceptRefundCharge(
        @Body refundRequest: RefundRequest
    ): GenericResponse<RestResponse<ChangeTicketDetails>>

    @GET("flight/booking/history/detail")
    suspend fun getActualBookingDetails(
        @Query("bookingCode") bookingCode: String,
        @Query("pnrCode") pnrCode: String
    ): GenericResponse<RestResponse<FlightHistory>>

    @GET("flight/reissue/eligibility")
    suspend fun checkReissueEligibility(
        @Header("accesstoken") key: String,
        @Query("bookingCode") bookingCode: String,
    ): GenericResponse<RestResponse<ReissueEligibilityResponse>>

    @GET("flight/refund/eligible-travellers")
    suspend fun refundEligibleTravellers(
        @Header("accesstoken") key: String,
        @Query("bookingCode") bookingCode: String,
    ): GenericResponse<RestResponse<RefundEligibleTravellerResponse>>


    @GET("flight/refund/quotation")
    suspend fun refundQuotation(
        @Header("accesstoken") key: String,
        @Query("eTickets") eTickets: String?,
        @Query("bookingCode") bookingCode: String,
    ): GenericResponse<RestResponse<RefundQuotationResponse>>
    @POST("flight/refund/confirm")
    suspend fun confirmRefundRequest(
        @Header("accesstoken") key: String,
        @Body refundSearchIdBody: RefundSearchIdBody
    ): GenericResponse<RestResponse<VRRConfirmResponse>>

    @GET("flight/void/eligible-travellers")
    suspend fun voidEligibleTravellers(
        @Header("accesstoken") key: String,
        @Query("bookingCode") bookingCode: String,
    ): GenericResponse<RestResponse<RefundEligibleTravellerResponse>>

    @GET("flight/void/quotation")
    suspend fun voidQuotation(
        @Header("accesstoken") key: String,
        @Query("eTickets") eTickets: String?,
        @Query("bookingCode") bookingCode: String,
    ): GenericResponse<RestResponse<VRRQuotationResponse>>

    @POST("flight/void/confirm")
    suspend fun confirmVoidRequest(
        @Header("accesstoken") key: String,
        @Body refundSearchIdBody: VoidSearchIdBody
    ): GenericResponse<RestResponse<VRRConfirmResponse>>

    @POST("flight/void/quotation-cancel")
    suspend fun cancelVoidRequest(
        @Header("accesstoken") key: String,
        @Body refundSearchIdBody: VoidSearchIdBody
    ): GenericResponse<RestResponse<Any>>

    @POST("flight/refund/quotation-cancel")
    suspend fun cancelRefundRequest(
        @Header("accesstoken") key: String,
        @Body refundSearchIdBody: RefundSearchIdBody
    ): GenericResponse<RestResponse<Any>>
}


