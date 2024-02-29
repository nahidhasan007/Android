package net.sharetrip.b2b.view.flight.history.historydetails

import android.util.Log
import net.sharetrip.b2b.model.EmptyResponse
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.view.flight.history.model.FlightHistoryActionResponse
import net.sharetrip.b2b.view.flight.history.model.IssueTicketDetails
import net.sharetrip.b2b.view.flight.history.model.IssueTicketResponse
import net.sharetrip.b2b.view.flight.history.model.RefundEligibleTravellerResponse
import net.sharetrip.b2b.view.flight.history.model.RefundQuotationResponse
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueEligibilityResponse

class FlightHistoryDetailsRepo(private val flightEndPoint: FlightEndPoint) {

    init {

    }

    suspend fun getHistoryActionVisibility(
        bookingCode: String,
        pnrCode: String
    ): GenericResponse<RestResponse<FlightHistoryActionResponse>> {
        return flightEndPoint.getFlightHistoryButton(bookingCode, pnrCode)
    }

    suspend fun issueTicket(issueTicketDetails: IssueTicketDetails): GenericResponse<RestResponse<IssueTicketResponse>> {
        return flightEndPoint.issueTicket(
            issueTicketDetails.bookingCode!!,
            issueTicketDetails.pnrCode!!
        )
    }

    suspend fun cancelBooking(bookingCode: String): GenericResponse<RestResponse<EmptyResponse>> {
        return flightEndPoint.cancelBooking(
            bookingCode
        )
    }

    suspend fun checkReissueEligibility(bookingCode: String): GenericResponse<RestResponse<ReissueEligibilityResponse>> {
        return try {
            val accessToken = AppSharedPreference.accessToken
            val output = flightEndPoint.checkReissueEligibility(key = accessToken, bookingCode = bookingCode)

            Log.d("NahidDebug123", "inside normal: ${output.toString()}")
            output
        } catch (x: Exception) {
            x.printStackTrace()
            Log.d("NahidDebug123", "inside crash: ${x.message}")
            BaseResponse.UnknownError(x)
        }
    }
    suspend fun checkRefundEligibleTravellers(bookingCode: String): GenericResponse<RestResponse<RefundEligibleTravellerResponse>> {
        return try {
            val accessToken = AppSharedPreference.accessToken
            val output = flightEndPoint.refundEligibleTravellers(key = accessToken, bookingCode = bookingCode)

            Log.d("NahidDebug123", "inside normal: ${output.toString()}")
            output
        } catch (x: Exception) {
            x.printStackTrace()
            Log.d("NahidDebug123", "inside crash: ${x.message}")
            BaseResponse.UnknownError(x)
        }
    }

    suspend fun refundQuotationRequest(bookingCode: String) : GenericResponse<RestResponse<RefundQuotationResponse>> {
        return  try {
            val token = AppSharedPreference.accessToken
            val response = flightEndPoint.refundQuotation(key = token, null, bookingCode=bookingCode)
            Log.d("NahidDebug123", "inside normal: ${response.toString()}")
            response
        }
        catch (x: Exception) {
            x.printStackTrace()
            BaseResponse.UnknownError(x)
        }
    }

    suspend fun checkVoidEligibleTravellers(bookingCode: String): GenericResponse<RestResponse<RefundEligibleTravellerResponse>> {
        return try {
            val accessToken = AppSharedPreference.accessToken
            val output = flightEndPoint.voidEligibleTravellers(key = accessToken, bookingCode = bookingCode)

            Log.d("NahidDebug123", "inside normal: ${output.toString()}")
            output
        } catch (x: Exception) {
            x.printStackTrace()
            Log.d("NahidDebug123", "inside crash: ${x.message}")
            BaseResponse.UnknownError(x)
        }
    }

}