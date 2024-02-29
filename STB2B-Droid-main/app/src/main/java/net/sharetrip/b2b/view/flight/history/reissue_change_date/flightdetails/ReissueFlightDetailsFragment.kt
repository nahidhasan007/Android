package net.sharetrip.b2b.view.flight.history.reissue_change_date.flightdetails

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import net.sharetrip.b2b.R
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.databinding.CommonSuccessLayoutBinding
import net.sharetrip.b2b.databinding.FragmentReissueFlightDetailsBinding
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.util.DateUtil
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.util.showToast
import net.sharetrip.b2b.view.flight.booking.model.PriceBreakdown
import net.sharetrip.b2b.view.flight.booking.ui.flightdetails.FlightDetailsFragment
import net.sharetrip.b2b.view.flight.history.model.PriceDetails
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ReissueChangeDateSharedViewModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.flightlist.FlightListFragment
import net.sharetrip.b2b.view.flight.history.reissue_change_date.flightlist.FlightListFragment.Companion.ARG_REISSUE_FLIGHT_SEARCH_MODEL
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.Detail
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.FlightX
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlightSearch
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.Segment
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.getParcelableCompat
import net.sharetrip.b2b.widgets.BaseFragment
import java.util.concurrent.TimeUnit

class ReissueFlightDetailsFragment : BaseFragment<FragmentReissueFlightDetailsBinding>() {
    companion object {
        const val TAG = "ReissueFlightDetails"
        const val ARA_ITEM_FLIGHTS_SEGMENT = "ARA_ITEM_FLIGHTS_SEGMENT"
        const val PRICE_BREAKDOWN = "PRICE_BREAKDOWN"
        const val MODIFY_HISTORY = "MODIFY_HISTORY"
        const val HISTORY_RESPONSE = "HISTORY_RESPONSE"
        const val REISSUE_SEARCH_ID = "REISSUE_SEARCH_ID"
        const val SEQUENCE_CODE = "SEQUENCE_CODE"
        const val QUOTATION_PARAM = "QUOTATION_PARAM"
    }


    lateinit var priceDetails: PriceDetails
    private lateinit var quotationDialog: Dialog
    private val sharedViewModel: ReissueChangeDateSharedViewModel by activityViewModels()
    private val reissueFlightSearch: ReissueFlightSearch by lazy {
        requireArguments().getParcelableCompat(
            ARG_REISSUE_FLIGHT_SEARCH_MODEL,
            ReissueFlightSearch::class.java
        )!!
    }
    private val reissueApiService by lazy {
        ServiceGenerator.createService(ReissueApiService::class.java)
    }
    private val flights: FlightX by lazy {
        requireArguments().getParcelableCompat(
            FlightDetailsFragment.ARG_ITEM_FLIGHTS,
            FlightX::class.java
        )!!
    }
    private val searchId: String by lazy {
        requireArguments().getString(FlightListFragment.ARG_REISSUE_SEARCH_ID, "")
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ReissueFlightDetailsVMFactory(
                reissueFlightSearch,
                reissueApiService,
                AppSharedPreference.accessToken
            )
        )[ReissueFlightDetailsVm::class.java]
    }


    // bookingCode
    val bookingCode: String by lazy { sharedViewModel.bookingCode }
    val reissueSearchId: String by lazy { viewModel.reissueSearchId }
    val sequenceCode: String? by lazy { flights.sequenceCode }
    private var timer: CountDownTimer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initArguments()
        bindingView.flights = flights
        bindingView.flightSearch = reissueFlightSearch


        viewModel.reissueSearchId =
            searchId // sharedViewModel.reissueEligibilityResponse?.reissueSearch?.reissueSearchId!!
        sharedViewModel.reissueSearchId = searchId
        if (flights.reissueSequenceCode != null) {
            viewModel.reissueSequenceCode = flights.reissueSequenceCode!!
            sharedViewModel.reissueSequenceCode = flights.reissueSequenceCode!!
        } else {
            viewModel.reissueSequenceCode = flights.manualSequenceCode!!
            sharedViewModel.reissueSequenceCode = flights.manualSequenceCode!!
        }
        flights.sequenceCode


        val priceBreakdown = flights.priceBreakdown
        val listPriceBreakdown = priceBreakdown.details?.map { detail: Detail ->
            PriceBreakdown(
                type = detail.type, baseFare = detail.baseFare, tax = detail.tax,
                numberPaxes = detail.numberPaxes, currency = detail.currency, total = detail.total
            )
        }?.toList()
        priceDetails = PriceDetails(
            null,
            flights.currency,
            listPriceBreakdown,
            null,
            priceBreakdown.discountAmount,
            priceBreakdown.advanceIncomeTax,
            priceBreakdown.originPrice,
            null,
            priceBreakdown.originPrice,
            priceBreakdown.total
        )

        bindingView.viewModel = viewModel
        bindingView.priceDetails = priceDetails
        viewModel.checkReissueConfirm.observe(viewLifecycleOwner) {
            if (it) {
                sharedViewModel.reissueConfirmCheck.postValue(true)
                successQuotationDialog()
            }
        }

        viewModel.manualCancelResponseCheck.observe(viewLifecycleOwner) {
            if (it) {
                sharedViewModel.manualCancelCheck.postValue(true)
                for (i in 1..7) {
                    findNavController().navigateUp()
                }
            }
        }

        viewModel.approveObserver.observe(viewLifecycleOwner) {
            if (it) {
                agreeAndConfirmDialog()
            }
        }
        if (sharedViewModel.reissueEligibilityResponse?.reissueSearch?.status == "QUOTED") {
            viewModel.isQuoted = true
            bindingView.reissueRemainingTime.visibility = View.VISIBLE
            bindingView.quotationMsg.text =
                sharedViewModel.reissueEligibilityResponse!!.reissueSearch?.msg
            manualReissueExpiredAt(sharedViewModel.quotationExpiryAt)
        } else {
            bindingView.reissueRemainingTime.visibility = View.GONE
        }

        val listOfAny: List<Any> = flights.segments.map { segment: Segment ->
            segment as Any
        }.toList()

        viewModel.flights = this.flights
        flights
        bindingView.recyclerViewFlightDetails.adapter =
            ReissueFlightDetailsAdapter(vm = viewModel, items = listOfAny)
        bindingView.recyclerViewFlightDetails.layoutManager = LinearLayoutManager(requireContext())

//        initObservers()
    }

//    private fun initObservers() {
//        sharedViewModel.selectionQuotation.observe(viewLifecycleOwner, EventObserver {
//            if(it) {
//
//                viewModel.onClickQuotationSelectApi(bookingCode, reissueSearchId, sequenceCode) // onclickReissueManualConfirm(sharedViewModel.reissueEligibilityResponse!!.reissueSearch?.reissueSearchId!!, flights.reissueSequenceCode!!)
//            }
//        })
//
//        viewModel.gotoSegment.observe(viewLifecycleOwner, EventObserver{ somePosition: Int ->
//            try {
//                timer?.cancel()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            showToast(requireContext(), "On see details click!")
//            val bundle = Bundle()
//            val someSegment = flights.segments[somePosition]
//            bundle.putParcelable(PRICE_BREAKDOWN, flights.priceBreakdown)
////            bundle.putParcelable(ARA_ITEM_FLIGHTS_SEGMENT, someSegment)
//            findNavController().navigate(
//                R.id.action_flightDetailsFragment2_to_PriceBreakDownFragment2,
//                bundle
//            )
//        })
//
//        viewModel.reissueQuotationRequestSelectResponse.observe(viewLifecycleOwner, EventObserver {someResponse ->
//           successQuotationDialog(msg = someResponse?.response?.message?: defaultMsg )
//
//        })
//    }


//    override fun onDestroyView() {
//        super.onDestroyView()
//        _bindingView = null
//    }

    override fun layoutId(): Int = R.layout.fragment_reissue_flight_details
    override fun getViewModel(): BaseViewModel? = null

    override fun initOnCreateView() {
        bindingView.viewModel = viewModel
//        sharedViewModel.selectionQuotation.observe(viewLifecycleOwner, EventObserver {
//            if (it) {
//
//                viewModel.onClickQuotationSelectApi(
//                    bookingCode,
//                    reissueSearchId,
//                    sequenceCode
//                ) // onclickReissueManualConfirm(sharedViewModel.reissueEligibilityResponse!!.reissueSearch?.reissueSearchId!!, flights.reissueSequenceCode!!)
//            }
//        })


        bindingView.selectFlight.setOnClickListener() {
            val bundle = Bundle()
            bundle.putString(REISSUE_SEARCH_ID, reissueSearchId)
            bundle.putString(SEQUENCE_CODE, sequenceCode)
            findNavController().navigate(
                R.id.action_flightDetailsFragment2_to_reissueManualQuotationFragment,
                bundleOf(QUOTATION_PARAM to bundle)
            )
        }

        bindingView.textViewPriceBreakdown.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(PRICE_BREAKDOWN, flights.priceBreakdown)
            findNavController().navigate(
                R.id.action_flightDetailsFragment2_to_PriceBreakDownFragment2,
                bundle
            )
        }

        viewModel.gotoSegment.observe(viewLifecycleOwner, EventObserver { somePosition: Int ->
            try {
                timer?.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val bundle = Bundle()
            bundle.putParcelable(PRICE_BREAKDOWN, flights.priceBreakdown)
            findNavController().navigate(
                R.id.action_flightDetailsFragment2_to_PriceBreakDownFragment2,
                bundle
            )
        })

    }

    private val defaultMsg: String by lazy {
        requireContext().getString(R.string.your_reissue_request_has_been_submitted_successfully_it_will_come_into_effect_very_soon)
    }

    private fun successQuotationDialog(msg: String = defaultMsg) {

        val commonSuccessLayoutBinding =
            CommonSuccessLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        quotationDialog = Dialog(requireContext())
        quotationDialog.setContentView(commonSuccessLayoutBinding.root)

        commonSuccessLayoutBinding.goToHome.text = msg
        commonSuccessLayoutBinding.actualCross.setOnClickListener {
            for(i in 1..7){
                findNavController().navigateUp()
            }
            quotationDialog.dismiss()
        }

        quotationDialog.setCanceledOnTouchOutside(false)
        quotationDialog.show()
    }

    private fun agreeAndConfirmDialog() {
        quotationDialog = Dialog(requireContext())
        quotationDialog.setContentView(R.layout.agree_and_confirm_layout)
        val agreeAndConfirm = quotationDialog.findViewById<Button>(R.id.agreeBtn)
        val cancel = quotationDialog.findViewById<Button>(R.id.closeBtn)
        agreeAndConfirm.setOnClickListener() {
            viewModel.onclickReissueManualConfirm()
            quotationDialog.dismiss()
        }
        cancel.setOnClickListener() {
            quotationDialog.dismiss()
        }
        quotationDialog.setCanceledOnTouchOutside(false)
        quotationDialog.show()
    }

    private fun manualReissueExpiredAt(expiresAt: String) {
        val currentTime = System.currentTimeMillis()
        val remainingTicktingTime =
            DateUtil.parseAPIDateTimeToMillisecond(expiresAt)
        if (remainingTicktingTime - currentTime > 0) {
            timer = object : CountDownTimer(remainingTicktingTime - currentTime, 1000) {
                override fun onTick(p0: Long) {
                    val hours = TimeUnit.MILLISECONDS.toHours(p0)
                    val day = hours/24
                    val hour = hours%24
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(p0) % 60
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(p0) % 60
                    bindingView.remainingTimeMsg.text =
                        " " + day + 'd' + ":$hour" + 'h' + ":$minutes" + 'm' + ":$seconds" + 's'
                }

                override fun onFinish() {
                    bindingView?.reissueRemainingTime?.visibility = View.GONE
                }
            }
            timer?.start()
        } else {
            bindingView.reissueRemainingTime.visibility = View.GONE
        }
    }


}
