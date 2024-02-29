package net.sharetrip.b2b.view.flight.history.downloadvowcher

import android.net.Uri
import androidx.databinding.ObservableDouble
import com.google.gson.Gson
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.network.ServiceGenerator.API_BASE_URL
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.view.flight.history.model.DownloadVoucher
import net.sharetrip.b2b.view.flight.history.model.FlightHistory
import net.sharetrip.b2b.view.flight.history.model.TravellerItem

class DownloadVoucherVM : BaseViewModel() {
    val downloadVoucher = DownloadVoucher()
    var totalPrice = ObservableDouble()

    fun getUri(): String {
        return Uri.parse(API_BASE_URL+"flight/booking/download-voucher")
            .buildUpon()
            .appendQueryParameter("bookingCode", downloadVoucher.bookingCode)
            .appendQueryParameter("pnrCode", downloadVoucher.pnrCode)
            .appendQueryParameter("hidePrice", downloadVoucher.hidePrice.toString())
            .appendQueryParameter("showTotalPrice", downloadVoucher.showTotalPrice.toString())
            .appendQueryParameter("attachCompany", downloadVoucher.attachCompany.toString())
            .appendQueryParameter(
                "applyCustomPricing",
                downloadVoucher.applyCustomPricing.toString()
            )
            .appendQueryParameter("usertoken", AppSharedPreference.accessToken)
            .appendQueryParameter("customPricing", Gson().toJson(downloadVoucher.customPricing))
            .build().toString()
    }

    fun setVoucherDetails(flightHistory: FlightHistory) {
        downloadVoucher.bookingCode = flightHistory.bookingCode
        downloadVoucher.pnrCode = flightHistory.pnrCode
        flightHistory.priceBreakdown?.originPrice?.let { totalPrice.set(it) }

        flightHistory.priceBreakdown?.details?.forEach {
            val travellerItem = TravellerItem(it.baseFare * it.numberPaxes, it.tax * it.numberPaxes)
            when {
                it.type.equals("Adult", ignoreCase = true) -> downloadVoucher.customPricing.adult =
                    travellerItem
                it.type.equals("Child", ignoreCase = true) -> downloadVoucher.customPricing.child =
                    travellerItem
                it.type.equals(
                    "Infant",
                    ignoreCase = true
                ) -> downloadVoucher.customPricing.infant = travellerItem
            }
        }
        setCustomTotalPrice()
    }

    fun addCustomPrice(traveller: TravellerItem, type: String) {
        when {
            type.equals("Adult", ignoreCase = true) -> downloadVoucher.customPricing.adult =
                traveller
            type.equals("Child", ignoreCase = true) -> downloadVoucher.customPricing.child =
                traveller
            type.equals("Infant", ignoreCase = true) -> downloadVoucher.customPricing.infant =
                traveller
        }
        setCustomTotalPrice()
    }

    private fun setCustomTotalPrice() {
        val customPrices = downloadVoucher.customPricing
        totalPrice.set(
            customPrices.adult.baseFare + customPrices.adult.taxFare +
                    customPrices.child.baseFare + customPrices.child.taxFare +
                    customPrices.infant.baseFare + customPrices.infant.taxFare
        )
    }
}