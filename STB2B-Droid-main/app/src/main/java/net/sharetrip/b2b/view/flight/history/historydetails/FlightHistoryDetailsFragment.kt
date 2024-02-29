package net.sharetrip.b2b.view.flight.history.historydetails

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentFlightHistoryDetailsBinding
import net.sharetrip.b2b.databinding.VrrRequestedDialogBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.*
import net.sharetrip.b2b.view.flight.booking.ui.flightdetails.FlightDetailsFragment
import net.sharetrip.b2b.view.flight.history.baggagedetails.BaggageDetailsFragment
import net.sharetrip.b2b.view.flight.history.confirmation.ConfirmationFragment.Companion.ARG_FROM_WHERE
import net.sharetrip.b2b.view.flight.history.faredetails.FragmentFareDetails.Companion.ARGS_PRICE_BREAKDOWN
import net.sharetrip.b2b.view.flight.history.historylist.FlightHistoryListFragment
import net.sharetrip.b2b.view.flight.history.model.FlightHistory
import net.sharetrip.b2b.view.flight.history.model.RefundQuotationResponse
import net.sharetrip.b2b.view.flight.history.model.VRRQuotationResponse
import net.sharetrip.b2b.view.flight.history.passengerdetails.PassengerDetailsFragment.Companion.ARGS_PASSENGER_DETAILS
import net.sharetrip.b2b.view.flight.history.refund.RefundVoidSharedViewModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ReissueChangeDateSharedViewModel
import net.sharetrip.b2b.view.topup.ui.paymentmethod.PaymentMethodFragment

class FlightHistoryDetailsFragment : Fragment(), View.OnClickListener {
    lateinit var bindingView: FragmentFlightHistoryDetailsBinding
    lateinit var flightHistory: FlightHistory

    private val sharedViewModel: ReissueChangeDateSharedViewModel by activityViewModels()
    private val refundSVM: RefundVoidSharedViewModel by activityViewModels()


    private val viewModel by lazy {
        val endPoint = ServiceGenerator.createService(FlightEndPoint::class.java)

        flightHistory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(
                FlightHistoryListFragment.FLIGHT_HISTORY_DETAILS,
                FlightHistory::class.java
            )!!
        } else {
            @Suppress("DEPRECATION")
            requireArguments().getParcelable(FlightHistoryListFragment.FLIGHT_HISTORY_DETAILS)!!
        }

        FlightHistoryDetailsVMFactory(
            endPoint,
            FlightHistoryDetailsRepo(endPoint),
            flightHistory
        ).create(FlightHistoryDetailsVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView =
            FragmentFlightHistoryDetailsBinding.inflate(layoutInflater, container, false)
        bindingView.viewModel = viewModel
        bindingView.flightHistory = flightHistory
        sharedViewModel.flightHistory = flightHistory
        refundSVM.flightHistory = flightHistory
        if(flightHistory.domestic==1){
            viewModel.isDomestic = true
        }

        for (segments in flightHistory.segments!!) {
            for (segments in segments.segment!!) {
                if (segments.flightNumber != null) {
                    sharedViewModel.flightNoBefore =
                        "${segments.airlinesCode}${segments.flightNumber}"
                    break
                }
            }
        }

        viewModel.moveToConfirmation.observe(viewLifecycleOwner, EventObserver {
            findNavController().navigate(
                R.id.action_to_confirmation,
                bundleOf(
                    PaymentMethodFragment.ARG_IS_CONFIRMED to it.first,
                    ARG_FROM_WHERE to it.second
                )
            )
        })

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.includeFlightDetails.layoutArrow.setOnClickListener(this)
        bindingView.includePassengerDetails.layoutArrow.setOnClickListener(this)
        bindingView.includeFareDetails.layoutArrow.setOnClickListener(this)
        bindingView.includeBaggageDetails.layoutArrow.setOnClickListener(this)
        bindingView.includeActionButtons.btnDownloadVoucher.setOnClickListener(this)
        bindingView.includeActionButtons.buttonTemporaryCancel.setOnClickListener(this)
        bindingView.includeActionButtons.buttonVoid.setOnClickListener(this)
        bindingView.includeActionButtons.buttonRefund.setOnClickListener(this)
        bindingView.includeActionButtons.btnCancelBooking.setOnClickListener(this)
        bindingView.includeActionButtons.btnDateChange.setOnClickListener(this)

        initObservers()

        return bindingView.root
    }

    private fun initObservers() {
        viewModel.showMessage.observe(viewLifecycleOwner, EventObserver { msg ->
            Log.d("FlightHistoryError", msg)
            showToast(requireContext(), msg)
        })

        viewModel.reissueEligibilityResponse.observe(viewLifecycleOwner,
            EventObserver { eligibilityResponse ->
                if (eligibilityResponse.automationSupported) {
                    findNavController().navigate(
                        // this is automation section
                        R.id.action_flight_details_to_reissue_change_date,
                        bundleOf(
                            ARG_REISSUE_ELIGIBILITY_RESPONSE to eligibilityResponse,
                            BOOKING_CODE to flightHistory.bookingCode
                        )
                    )
                } else {
                    if (eligibilityResponse.reissueSearch == null) {
                        findNavController().navigate(
                            R.id.action_flight_details_to_reissue_change_date,
                            bundleOf(
                                ARG_REISSUE_ELIGIBILITY_RESPONSE to eligibilityResponse,
                                BOOKING_CODE to flightHistory.bookingCode
                            )
                        )
                    } else {
                        if (eligibilityResponse.reissueSearch.status == "REQUESTED" || eligibilityResponse.reissueSearch.status == "CLIENT_APPROVED") {
                            bindingView.reissueCardViewQuoted.visibility = View.GONE
                            bindingView.reissueCardView.visibility = View.VISIBLE
                            bindingView.reissueSearchMsg.text =
                                eligibilityResponse.reissueSearch.msg
                        }
                        if (eligibilityResponse.reissueSearch.status == "REQUESTED" || eligibilityResponse.reissueSearch.status == "QUOTED") {
                            viewModel.reissueSearchId =
                                eligibilityResponse.reissueSearch.reissueSearchId
                        }
                        if (eligibilityResponse.reissueSearch.status == "QUOTED") {
                            bindingView.reissueCardView.visibility = View.GONE
                            bindingView.reissueCardViewQuoted.visibility = View.VISIBLE
                            bindingView.reissueSearchMsgQuoted.text =
                                eligibilityResponse.reissueSearch.msg + "Please Click here to check it"
                            val text = bindingView.reissueSearchMsgQuoted.text
                            val spannableString = SpannableString(text)
                            val clickableSpan = object : ClickableSpan() {
                                override fun onClick(view: View) {
                                    findNavController().navigate(
                                        R.id.action_flight_details_to_reissue_change_date,
                                        bundleOf(
                                            ARG_REISSUE_ELIGIBILITY_RESPONSE to eligibilityResponse,
                                            BOOKING_CODE to flightHistory.bookingCode
                                        )
                                    )
                                }
                            }
                            spannableString.setSpan(
                                clickableSpan,
                                44,
                                54,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            bindingView.reissueSearchMsgQuoted.text = spannableString
                            bindingView.reissueSearchMsgQuoted.movementMethod =
                                LinkMovementMethod.getInstance()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                eligibilityResponse.reissueSearch.status,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })

        viewModel.refundEligibilityResponse.observe(viewLifecycleOwner, EventObserver {
            if (it.skipSelection) {
                viewModel.getRefundQuotation(flightHistory.bookingCode)
            } else {
                refundSVM.refundEligibilityResponse = it
                findNavController().navigate(
                    R.id.action_flight_history_details_dest_to_refundTravellerSelection,
                    bundleOf(ARG_REFUND_ELIGIBILITY_RESPONSE to it)
                )
            }
        })

        viewModel.refundQuotationResponse.observe(viewLifecycleOwner){
            when (it.status) {

                "REQUESTED" -> {
                    bindingView.reissueCardViewQuoted.visibility = View.GONE
                    bindingView.reissueCardView.visibility = View.VISIBLE
                    bindingView.reissueSearchMsg.text = it.msg
                }

                "QUOTED" -> {
                    bindingView.reissueCardViewQuoted.visibility = View.VISIBLE
                    bindingView.reissueCardView.visibility = View.GONE
                    bindingView.reissueSearchMsgQuoted.text =
                        it.msg + " Please Click here to check it"
                    val text = bindingView.reissueSearchMsgQuoted.text
                    val spannableString = SpannableString(text)
                    val clickableSpan = object : ClickableSpan() {
                        override fun onClick(view: View) {

                            findNavController().navigate(
                                R.id.action_flight_history_details_dest_to_refundFlightSelection,
                                bundleOf(REFUND_QUOTATION_RESPONSE to it)
                            )
                        }
                    }

                    spannableString.setSpan(
                        clickableSpan,
                        44,
                        54,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    bindingView.reissueSearchMsgQuoted.text = spannableString
                    bindingView.reissueSearchMsgQuoted.movementMethod =
                        LinkMovementMethod.getInstance()
                }

                "CLIENT_APPROVED" -> {
                    Toast.makeText(requireContext(), it.status, Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.voidEligibilityResponse.observe(viewLifecycleOwner, EventObserver {
            if (it.skipSelection) {
                viewModel.getVoidQuotation(flightHistory.bookingCode)
            } else {
                refundSVM.voidEligibilityResponse = it
                findNavController().navigate(
                    R.id.action_flight_history_details_dest_to_travellerSelection,
                    bundleOf(ARG_VOID_ELIGIBILITY_RESPONSE to it)
                )
            }
        })

        viewModel.voidQuotationResponse.observe(viewLifecycleOwner) { quotation ->
            Log.e("Quotation", quotation.toString())

            when (quotation.status) {

                "REQUESTED" -> {
                    bindingView.reissueCardViewQuoted.visibility = View.GONE
                    bindingView.reissueCardView.visibility = View.VISIBLE
                    bindingView.reissueSearchMsg.text = quotation.msg
                }

                "QUOTED" -> {
                    findNavController().navigate(
                        R.id.action_flight_history_details_dest_to_voidPricing,
                        bundleOf("void_pricing" to quotation)
                    )
                }
                "EXPIRED" -> {
                    quotationRequestedAlertDialog(quotation)
                }
            }
        }

    }

    override fun onClick(v: View?) {
        when (v) {
            bindingView.includeFlightDetails.layoutArrow -> {
                findNavController().navigate(
                    R.id.action_flight_details_to_flight_segment, bundleOf(
                        SHOW_TOOLBAR to true, ARG_FLIGHT_HISTORY to flightHistory,
                    )
                )
            }

            bindingView.includePassengerDetails.layoutArrow -> {
                findNavController().navigate(
                    R.id.action_flight_details_to_passenger_details_dest, bundleOf(
                        ARGS_PASSENGER_DETAILS to getTravellersWithID(flightHistory.travellers!!),
                        FlightDetailsFragment.ARG_IS_DOMESTIC to viewModel.isDomestic
                    )
                )
            }

            bindingView.includeFareDetails.layoutArrow -> {

                flightHistory.priceBreakdown?.advanceIncomeTax = flightHistory.advanceIncomeTax

                findNavController().navigate(
                    R.id.action_flight_details_to_fare_details_dest, bundleOf(
                        ARGS_PRICE_BREAKDOWN to flightHistory.priceBreakdown
                    )
                )
            }

            bindingView.includeBaggageDetails.layoutArrow -> {
                findNavController().navigate(
                    R.id.action_flight_details_to_baggage_details_dest, bundleOf(
                        BaggageDetailsFragment.ARGS_BAGGAGE_INFO to flightHistory.baggageInfo
                    )
                )
            }

            bindingView.includeActionButtons.btnDownloadVoucher -> {
                findNavController().navigate(
                    R.id.action_flight_details_to_download_voucher_dest,
                    bundleOf(ARG_FLIGHT_HISTORY to flightHistory)
                )
            }

            bindingView.includeActionButtons.buttonTemporaryCancel -> {
                navigate(TICKET_ACTION_TEMPORARY_CANCEL)
            }

            bindingView.includeActionButtons.btnCancelBooking -> {
                showCancelDialog()
            }

            bindingView.includeActionButtons.btnDateChange -> {
                viewModel.onClickReissueChangeDate(flightHistory.bookingCode)
            }

            bindingView.includeActionButtons.buttonRefund -> {
                viewModel.refundEligibleTravellers(flightHistory.bookingCode)
            }

            bindingView.includeActionButtons.buttonVoid -> {
                viewModel.voidEligibleTravellers(flightHistory.bookingCode)
            }

        }
    }

    private fun showCancelDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(getString(R.string.cancel_booking))
        alertDialog.setMessage(getString(R.string.want_to_cancel_booking))
        alertDialog.setPositiveButton(getString(R.string.yes)) { _, _ ->
            viewModel.cancelBooking()
        }
        alertDialog.setNegativeButton(getString(R.string.cancel)) { _, _ ->
        }
        alertDialog.show()
    }

    private fun quotationRequestedAlertDialog(quotationResponse: VRRQuotationResponse) {
        val requestDialog = Dialog(requireContext())
        val binding = VrrRequestedDialogBinding.inflate(LayoutInflater.from(requireContext()))
        requestDialog.setContentView(binding.root)

        binding.quotationConfirmBody.text = quotationResponse.msg
        binding.CancelBtn.setOnClickListener {
            viewModel.cancelVoidRequest(quotationResponse.voidSearchId)
            requestDialog.dismiss()
        }
        binding.closeBtn.setOnClickListener {
            requestDialog.dismiss()
        }
        requestDialog.setCanceledOnTouchOutside(false)
        requestDialog.show()
    }


    private fun refundRequestedAlertDialog(quotationResponse: RefundQuotationResponse) {
        val requestDialog = Dialog(requireContext())
        val binding = VrrRequestedDialogBinding.inflate(LayoutInflater.from(requireContext()))
        requestDialog.setContentView(binding.root)

        binding.quotationConfirmBody.text = quotationResponse.msg
        binding.CancelBtn.setOnClickListener {
            viewModel.cancelRefundRequest(quotationResponse.refundSearchId)
            requestDialog.dismiss()
        }
        binding.closeBtn.setOnClickListener {
            requestDialog.dismiss()
        }
        requestDialog.setCanceledOnTouchOutside(false)
        requestDialog.show()
    }

    fun navigate(actionCode: Int) {
        findNavController().navigate(
            R.id.action_flight_details_to_select_passenger, bundleOf(
                ARG_FLIGHT_HISTORY to flightHistory,
                ACTION_CODE to actionCode
            )
        )
    }

    companion object {
        const val BOOKING_CODE = "BOOKING_CODE"
        const val ARG_REISSUE_ELIGIBILITY_RESPONSE = "REISSUE_ELIGIBILITY_RESPONSE"
        const val ARG_REFUND_ELIGIBILITY_RESPONSE = "ARG_REFUND_ELIGIBILITY_RESPONSE"
        const val ARG_VOID_ELIGIBILITY_RESPONSE = "ARG_VOID_ELIGIBILITY_RESPONSE"
        const val ARG_FLIGHT_HISTORY = "flight_history"
        const val ACTION_CODE = "ACTION_CODE"
        const val REFUND_QUOTATION_RESPONSE = "REFUND_QUOTATION_RESPONSE"
    }
}