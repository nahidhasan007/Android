package net.sharetrip.b2b.view.flight.history.bookingdetails

import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import net.sharetrip.b2b.R
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.databinding.BookingHistoryDetailsFragmentBinding
import net.sharetrip.b2b.view.flight.history.historylist.FlightHistoryListFragment
import net.sharetrip.b2b.view.flight.history.model.FlightHistory
import net.sharetrip.b2b.view.flight.history.model.ModifyHistory
import net.sharetrip.b2b.view.flight.history.reissue_change_date.flightdetails.ReissueFlightDetailsFragment
import net.sharetrip.b2b.view.flight.history.reissue_change_date.flightdetails.ReissueFlightDetailsFragment.Companion.HISTORY_RESPONSE
import net.sharetrip.b2b.widgets.BaseFragment

class BookingHistoryDetailsFragment : BaseFragment<BookingHistoryDetailsFragmentBinding>(),
    ClickViewDetails {

    private val viewModel: BookingHistoryDetailsVM by lazy {
        BookingHistoryDetailsVMF(
            historyResponse
        ).create(BookingHistoryDetailsVM::class.java)
    }

    override fun layoutId(): Int = R.layout.booking_history_details_fragment

    override fun getViewModel(): BaseViewModel? = null


    private lateinit var historyResponse: FlightHistory
    private var modifyHistoryAdapter: BookingHistoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gson = Gson()
        historyResponse = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(
                FlightHistoryListFragment.FLIGHT_HISTORY_DETAILS,
                FlightHistory::class.java
            )!!
        } else {
            @Suppress("DEPRECATION")
            requireArguments().getParcelable(FlightHistoryListFragment.FLIGHT_HISTORY_DETAILS)!!
        }
    }


    override fun initOnCreateView() {
        bindingView.viewModel = viewModel

        if (historyResponse.searchParams?.tripType == "Return") {
            bindingView.tripType.text = "Round Trip"
        } else {
            bindingView.tripType.text = historyResponse.searchParams?.tripType
        }
        bindingView.flyDate.text = historyResponse.getFlyDateAMPM()
        val historyBookingDetails = bindingView.historyBookingDetails
        modifyHistoryAdapter =
            historyResponse.modificationHistories?.let {
                BookingHistoryAdapter(
                    historyResponse.modificationHistories!!,
                    historyResponse,
                    this
                )
            }

        bindingView.toolbar.setNavigationOnClickListener{
            findNavController().navigateUp()
        }

        historyBookingDetails.adapter = modifyHistoryAdapter
        modifyHistoryAdapter?.submitList(historyResponse.modificationHistories!!)

        bindingView.viewDetails.setOnClickListener() {
            findNavController().navigate(
                R.id.action_bookingHistoryDetailsFragment_to_flight_history_details_dest,
                bundleOf(FlightHistoryListFragment.FLIGHT_HISTORY_DETAILS to historyResponse)
            )
        }


        bindingView.lineView.isVisible = historyResponse.modificationHistories?.size!! > 0
        val layoutManager = bindingView.historyBookingDetails.layoutManager
        if (layoutManager != null && layoutManager.itemCount > 0) {
            val lastItemView = layoutManager.findViewByPosition(layoutManager.itemCount - 1)
            val lastItemHeight = lastItemView?.height ?: 0

            // Set the height of the lineView to match the last item's height
            bindingView.lineView.layoutParams.height = lastItemHeight
            bindingView.lineView.requestLayout()
        }
    }


    //    override fun clickViewDetails(modifyHistory: ModifyHistory) {
//        findNavController().navigateSafe(
//            R.id.action_bookingHistoryDetails_to_modifyHistoryDetails2,
//            bundleOf(ARG_MODIFY_HISTORY to modifyHistory, ARG_FLIGHT_HISTORY_DETAILS_RESPONSE to historyResponse)
//        )
//
//    }
    companion object {
        const val ARG_FLIGHT_HISTORY_DETAILS_RESPONSE = "ARG_FLIGHT_HISTORY_DETAILS_RESPONSE"
        const val ARG_FLIGHT_BOOKING_HISTORY_ITEM = "ARG_FLIGHT_BOOKING_HISTORY_ITEM"
        const val ARG_MODIFY_HISTORY = "ARG_MODIFY_HISTORY"

    }

    override fun onClickViewDetails(modifyHistory: ModifyHistory, history: FlightHistory) {
        val bundle = Bundle()
        bundle.putParcelable(
            ReissueFlightDetailsFragment.MODIFY_HISTORY,
            modifyHistory
        )
        bundle.putParcelable(HISTORY_RESPONSE, history)
        findNavController().navigate(
            R.id.action_bookingHistoryDetailsFragment_to_reissueDetailsFragment,
            bundle
        )
    }
}