package net.sharetrip.b2b.view.flight.history.singledatecalendar


import androidx.lifecycle.MutableLiveData
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.widgets.BaseOperationalVm
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.AdvanceSearchResponse
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService

class CalendarVM : BaseOperationalVm() {
    private val reissueApiService by lazy {
        ServiceGenerator.createService(ReissueApiService::class.java)
    }
    val advanceSearchResponse = MutableLiveData<AdvanceSearchResponse>()

    fun getFlightCalenderPrice(from: String, to: String, departOne: String, departTwo: String) {
        executeSuspendedCodeBlock(CALENDER_PRICE) {
            reissueApiService.getFlightCalendarPriceInfo(
                from,
                to,
                departOne,
                departTwo
            )
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        if (operationTag == CALENDER_PRICE){
            advanceSearchResponse.value = ( data.body as RestResponse<*>).response as AdvanceSearchResponse
        }
    }

    private companion object {
        const val CALENDER_PRICE = "calender_price"
    }
}