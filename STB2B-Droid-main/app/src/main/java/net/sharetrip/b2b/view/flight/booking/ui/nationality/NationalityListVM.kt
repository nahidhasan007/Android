package net.sharetrip.b2b.view.flight.booking.ui.nationality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.view.flight.booking.model.Nationality

class NationalityListVM(private val repo: NationalityListRepo) : ViewModel() {
    val showMessage: LiveData<String>
        get() = _showMessage
    private val _showMessage = MutableLiveData<String>()

    val nationalityList: LiveData<List<Nationality>>
        get() = _countryList
    private var _countryList = MutableLiveData<List<Nationality>>()

    init {
        viewModelScope.launch {
            val result = repo.getAllCountryList()
            operationOnResponse(result)
        }
    }

    private fun operationOnResponse(data: GenericResponse<RestResponse<List<Nationality>>>) {
        when(data) {
            is BaseResponse.Success ->
                _countryList.value = data.body.response

            is BaseResponse.ApiError ->
                _showMessage.value = data.errorBody.message

            is BaseResponse.NetworkError ->
                _showMessage.value = MsgUtils.networkErrorMsg

            is BaseResponse.UnknownError ->
                _showMessage.value = MsgUtils.unKnownErrorMsg
        }
    }
}
