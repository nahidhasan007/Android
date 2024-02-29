package net.sharetrip.b2b.view.flight.booking.ui.proposal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericError
import net.sharetrip.b2b.network.NetworkState
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.view.flight.booking.model.FlightDetails
import net.sharetrip.b2b.view.flight.booking.model.ProposalDownloadResponse

class FlightProposalViewModel(val repo: FlightProposalRepo) : ViewModel() {
    val dataState = MutableLiveData<Event<NetworkState>>()
    val fileMap = MutableLiveData<Event<Map<String, String>>>()

    fun getDownloadLinkFromServer(flightDetails: FlightDetails) {
        viewModelScope.launch {
            dataState.value = Event(NetworkState.LOADING)
            val data = repo.downLoadProposal(flightDetails)
            handleApiResponse(data)
        }
    }

    private fun handleApiResponse(data: BaseResponse<RestResponse<ProposalDownloadResponse>, GenericError>) {
        when (data) {
            is BaseResponse.Success -> {
                dataState.value = Event(NetworkState.LOADED)
                val array: ProposalDownloadResponse = data.body.response
                fileMap.value = Event(array.data)
            }

            is BaseResponse.ApiError ->
                dataState.value = Event(NetworkState.error(data.errorBody.message))

            is BaseResponse.NetworkError ->
                dataState.value = Event(NetworkState.error(data.error.localizedMessage))

            is BaseResponse.UnknownError ->
                dataState.value = Event(NetworkState.error(data.error.localizedMessage))
        }
    }
}
