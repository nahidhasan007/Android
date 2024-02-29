package net.sharetrip.b2b.view.flight.history.reissue_change_date.flightlist

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import net.sharetrip.b2b.R
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.view.flight.booking.model.Airline
import net.sharetrip.b2b.view.flight.booking.model.FilterParams
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ErrorType
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.SortingType
import net.sharetrip.b2b.widgets.BaseOperationalVm
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlightSearch
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.*
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService
import java.util.Locale

class FlightListViewModel(
    val apiService: ReissueApiService,
    val flightSearch: ReissueFlightSearch,
    private val repository: FlightListRepository,
    val quotationRequestBody: ReissueQuotationRequestBody,
    val token : String
) : BaseOperationalVm() {
    private val filterData = MutableStateFlow(ReissueQuotationFilterBody())

    val sortingObserver = MutableLiveData<SortingType>()
    val isShowSort = ObservableBoolean(false)
    var flightCount = 0
    var filterParams = FilterParams()
    val manualCancelResponse = MutableLiveData<Event<ReissueManualCancelResponse>>()
    val manualCancelResponseCheck = MutableLiveData(false)
    var onFilterClicked = MutableLiveData<Event<Boolean>>()
    var filter: Filters? = null



    val searchId: LiveData<String?>? get() = repository.flightPagingSource.value?.searchId

//    val expiresAt : LiveData<String?> get() = repository.flightPagingSource.value?.expiresAt?
    // get() = Transformations.switchMap(repository.flightPagingSource) { obj: FlightPagingSource -> obj.searchId }
    val expiresAt: LiveData<String>
        get() = Transformations.switchMap(repository.flightPagingSource) { obj: FlightPagingSource -> obj.expiresAt }

    val sessionId: LiveData<String>
        get() = Transformations.switchMap(repository.flightPagingSource) { obj: FlightPagingSource -> obj.sessionId }

    val isFirstPage: LiveData<Boolean>
        get() = Transformations.switchMap(repository.flightPagingSource) { obj: FlightPagingSource -> obj.isFirstPage }

    val filterDeal: LiveData<String>
        get() = Transformations.switchMap(repository.flightPagingSource) { obj: FlightPagingSource -> obj.filterDeal }

    val flightList = filterData.flatMapLatest { filterData ->
        repository.getFlightListFromRepository(quotationRequestBody, filterData)
    }.cachedIn(viewModelScope)

    val airlinesList: LiveData<List<Airline>>
        get() = Transformations.switchMap(repository.flightPagingSource) { obj: FlightPagingSource -> obj.filterAirlinesList() }

    val totalRecordCount: LiveData<Int>
        get() = Transformations.switchMap(repository.flightPagingSource) { obj: FlightPagingSource -> obj.totalRecord() }


    private fun filterSearchData(params: FilterParams?, searchId: String?) {
        filterData.update {
            ReissueQuotationFilterBody(
                reissueSearchId = searchId,
                page = 1,
                filters = params!!
            )
        }
    }

    fun handleSortingFilter(sortingValue: String, searchId: String) {
        filterParams.sort = sortingValue
        filterSearchData(filterParams, searchId)
    }

    fun handleAirlineFilter(airlineList: List<String>, searchId: String) {
        filterData.update { ReissueQuotationFilterBody() }
        filterParams.sort = sortingObserver.value.toString().toLowerCase(Locale.getDefault())
        filterParams.airlines = airlineList
        filterSearchData(filterParams, searchId)
    }

    fun handleFlightFilter(params: FilterParams, searchId: String) {
        filterParams = params
        filterParams.sort = sortingObserver.value.toString().toLowerCase(Locale.getDefault())
        filterSearchData(filterParams, searchId)
    }

    fun onSortingBtnClick(view: View) {
        if (view is AppCompatTextView) {
            when (view.id) {
                R.id.btnCheapest -> sortingObserver.value = SortingType.CHEAPEST
                R.id.btnFastest -> sortingObserver.value = SortingType.FASTEST
                R.id.btnEarliest -> sortingObserver.value = SortingType.EARLIEST
                R.id.sortOpenBtn -> isShowSort.set(!isShowSort.get())
            }
        }
    }

    fun setNumberOfFlight(numberOfFlight: Int) {
        flightCount = numberOfFlight
    }

    fun onClickFilter() {
        filter = repository.flightPagingSource.value!!.flightFilter()
        //filter?.refundableCustom = getRefundable()
        if (filter != null) {
            onFilterClicked.value = Event(true)
        }
    }

    fun onclickReissueManualCancel(reissueSearchId : String){
        val cancelManualReqBody = ReissueManualCancelBody(
            reissueSearchId
        )
        executeSuspendedCodeBlock(ApiCallingKey.ReissueCancel.name) {
            apiService.cancelReissueManaualRequest(token,cancelManualReqBody)
        }
    }

    fun onclickReissueManualConfirm(reissueSearchId : String, reissueSequenceCode: String){
        val confirmReissueBody = ConfirmReissueBody(
            reissueSearchId,
            reissueSequenceCode
        )
        executeSuspendedCodeBlock(ApiCallingKey.ReissueConfirm.name) {
            apiService.confirmReissue(token,confirmReissueBody)
        }
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when (operationTag) {
            ApiCallingKey.ReissueCancel.name -> {
                manualCancelResponse.postValue(Event(((data.body as RestResponse<*>).response as ReissueManualCancelResponse)))
                manualCancelResponseCheck.value = true
            }
        }
    }

    override fun onAnyError(operationTag: String, errorMessage: String, type: ErrorType?) {
        super.onAnyError(operationTag, errorMessage, type)
        when (operationTag) {
            ApiCallingKey.ReissueCancel.name -> {
                showMessage("Something Went Wrong")
            }
        }
    }

}