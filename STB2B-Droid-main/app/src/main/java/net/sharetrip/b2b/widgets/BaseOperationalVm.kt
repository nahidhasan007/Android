package net.sharetrip.b2b.widgets

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericError
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ErrorType
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.ERROR_MSG

abstract class BaseOperationalVm: BaseViewModel() {

    companion object {
        const val TAG = "OperationalVm"
    }

    protected fun executeSuspendedCodeBlock(
        operationTag: String = String(),
        codeBlock: suspend () -> GenericResponse<Any>
    ) {
        viewModelScope.launch {
            when (val data = codeBlock()) {
                is BaseResponse.Success -> {
                    onSuccessResponse(operationTag, data)
                }

                is BaseResponse.ApiError ->
                    onApiError(operationTag, data)

                is BaseResponse.NetworkError ->
                    onNetworkError(operationTag, data)

                is BaseResponse.UnknownError ->
                    onUnknownError(operationTag, data)
            }
        }
    }

    abstract fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>)

    protected fun onApiError(operationTag: String, result: BaseResponse.ApiError<GenericError>) {
        if (operationTag == String() && result.errorBody.message != "Please provide the access token.")
            showMessage(result.errorBody.message)
        onAnyError(operationTag, result.errorBody.message ?: ERROR_MSG, ErrorType.API_ERROR)
    }

    protected fun onNetworkError(operationTag: String, result: BaseResponse.NetworkError) {
        if (operationTag == String())
            showMessage(result.error.message)
        onAnyError(operationTag, result.error.message ?: ERROR_MSG, ErrorType.NETWORK_ERROR)
    }

    protected fun onUnknownError(operationTag: String, result: BaseResponse.UnknownError) {
        if (operationTag == String())
            showMessage(result.error.message)
        onAnyError(operationTag, result.error.message ?: ERROR_MSG, ErrorType.UNKNOWN_ERROR)
    }

    protected open fun onAnyError(operationTag: String, errorMessage: String, type: ErrorType?) {}

}