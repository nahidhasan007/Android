package net.sharetrip.b2b.view.flight.history.reissueviewdetails

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import net.sharetrip.b2b.network.BaseResponse
import net.sharetrip.b2b.network.ServiceGenerator.API_BASE_URL
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.view.flight.history.model.PriceBreakdown
import net.sharetrip.b2b.view.flight.history.model.DownLoadReissueVoucher
import net.sharetrip.b2b.view.flight.history.model.ModifyHistory
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ApiCallingKey
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService
import net.sharetrip.b2b.widgets.BaseOperationalVm

class ReissueDetailsVm(
    private val modifyHistory: ModifyHistory,
    private val token: String
) : BaseOperationalVm() {

    val isVoucherDownLoaded = MutableLiveData(false)
    var reissueCode = ""
    var priceBreakdown: PriceBreakdown? = null
    var downloadVoucher: DownLoadReissueVoucher? = null
    var hidePrice = 0
    var applyCustomPricing = 1
    var attachCompany = 1

    fun getUri(): String {
        Log.d("DownloadVoucher", downloadVoucher.toString())
        return Uri.parse(API_BASE_URL+"flight/reissue/download-voucher")
            .buildUpon()
            .appendQueryParameter("reissueCode", modifyHistory.reissueCode)
            .appendQueryParameter("hidePrice", hidePrice.toString())
            .appendQueryParameter("attachCompany", attachCompany.toString())
            .appendQueryParameter("applyCustomPricing", applyCustomPricing.toString())
            .appendQueryParameter("usertoken", AppSharedPreference.accessToken)
            .appendQueryParameter("customPricing", Gson().toJson(modifyHistory.priceBreakdown))
            .build().toString()
    }


    fun downloadVoucher(reissueCode: String, priceBreakdown: PriceBreakdown) {
        downloadVoucher?.hidePrice = false
        downloadVoucher?.attachCompany = true
        downloadVoucher?.reissueCode = reissueCode
        downloadVoucher?.applyCustomPricing = true
        downloadVoucher?.customPricing = priceBreakdown

/*        executeSuspendedCodeBlock(ApiCallingKey.DownloadVoucher.name) {
            apiService.downloadVoucher(
                key = token,
                reissueCode = reissueCode,
                hidePrice = 0,
                attachCompany = 1,
                applyCustomPricing = 1,
                customPricing = customPricing,

                )*/
    }

    override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        Log.d("NahidCheckOnSuccess!","It's working")
    }


 /*   override fun onSuccessResponse(operationTag: String, data: BaseResponse.Success<Any>) {
        when (operationTag) {
            ApiCallingKey.DownloadVoucher.name -> {
                val response = (data.body as RestResponse<*>).response
                if (response!=null) {
                    Log.d("NahidSuccess", "APi call succeed")
                    isVoucherDownLoaded.postValue(true)
                }
            }
        }
    }*/

//    override fun onAnyError(operationTag: String, errorMessage: String, type: ErrorType?) {
//        when (operationTag) {
//            ApiCallingKey.DownloadVoucher.name -> {
//                Log.d("NahidFailed", "APi call failed")
//                showMessage(errorMessage)
//            }
//        }
//    }

}