package net.sharetrip.b2b.view.topup.ui.paymentrequest

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableDouble
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.sharetrip.b2b.model.RestResponse
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.GenericResponse
import net.sharetrip.b2b.util.Event
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.util.ServiceTagConstants
import net.sharetrip.b2b.view.flight.booking.model.ImageUploadResponse
import net.sharetrip.b2b.view.topup.model.GateWay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class PaymentRequestVM(private val repo: PaymentRequestRepo) : ViewModel() {
    val dataLoading = ObservableBoolean(false)
    val gateWays = MutableLiveData<Event<List<GateWay>>>()
    val gateWay = ObservableField<GateWay>()
    var amount = 0
    val serviceCharge = ObservableDouble()
    val finalAmount = ObservableDouble()
    val addedAmount = ObservableBoolean(false)

    val showMessage: LiveData<String>
        get() = _showMessage
    private val _showMessage = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            dataLoading.set(true)
            val data = repo.getPaymentGateWay()
            gateWayResponseOperation(data)
            dataLoading.set(false)
        }

    }

    fun setGateWayDetails(gateWay: GateWay) {
        this.gateWay.set(gateWay)
        setTotalAmount("")
        //topUp = TopUp(amount, gateWay.id)
    }

    fun setTotalAmount(inputAmount: CharSequence) {
        if (inputAmount.isNotEmpty()) {
            amount = Integer.parseInt(inputAmount.toString())
            serviceCharge.set("%.2f".format(amount * (gateWay.get()!!.charge / 100)).toDouble())
        } else {
            amount = 0
            serviceCharge.set(0.0)
        }

        val totalAmount = amount - serviceCharge.get()
        finalAmount.set(totalAmount)
        addedAmount.set(inputAmount.isNotEmpty())
    }

    fun getUrlFromFilepath(file: File, mime: String) {
        val requestPhotoFile = file.asRequestBody(mime.toMediaTypeOrNull())
        val image = MultipartBody.Part.createFormData("uploadFile", file.name, requestPhotoFile)

        viewModelScope.launch {
            val data = repo.sendFile(image, ServiceTagConstants.mPayment)
            imageResponseOperation(data)
        }
    }

    private fun imageResponseOperation(
        data: GenericResponse<RestResponse<ImageUploadResponse>>
    ) {
        when (data) {
            is BaseResponse.Success -> {
                _showMessage.value = data.body.message
            }

            is BaseResponse.ApiError ->
                _showMessage.value = data.errorBody.message

            is BaseResponse.NetworkError ->
                _showMessage.value = MsgUtils.networkErrorMsg

            is BaseResponse.UnknownError ->
                _showMessage.value = MsgUtils.unKnownErrorMsg
        }
    }

    private fun gateWayResponseOperation(data: GenericResponse<RestResponse<List<GateWay>>>) {
        when (data) {
            is BaseResponse.Success ->
                gateWays.value = Event(data.body.response)

            is BaseResponse.ApiError ->
                _showMessage.value = data.errorBody.message

            is BaseResponse.NetworkError ->
                _showMessage.value = MsgUtils.networkErrorMsg

            is BaseResponse.UnknownError ->
                _showMessage.value = MsgUtils.unKnownErrorMsg
        }
    }
}