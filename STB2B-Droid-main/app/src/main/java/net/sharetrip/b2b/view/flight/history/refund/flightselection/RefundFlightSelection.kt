package net.sharetrip.b2b.view.flight.history.refund.flightselection

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.databinding.CommonSuccessLayoutBinding
import net.sharetrip.b2b.databinding.FragmentFlightSelectionBinding
import net.sharetrip.b2b.databinding.RefundConfirmLayoutBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.util.DateUtil
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.util.convertCurrencyToBengaliFormat
import net.sharetrip.b2b.view.flight.history.model.ButtonVRR
import net.sharetrip.b2b.view.flight.history.model.Flight
import net.sharetrip.b2b.view.flight.history.model.RefundQuotationResponse
import net.sharetrip.b2b.view.flight.history.refund.RefundVoidSharedViewModel
import net.sharetrip.b2b.view.flight.history.refund.travellerselection.RefundTravellerSelection.Companion.REFUND_QUOTATION_RESPONSE
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.getParcelableCompat
import net.sharetrip.b2b.widgets.BaseFragment
import java.util.concurrent.TimeUnit

class RefundFlightSelection : BaseFragment<FragmentFlightSelectionBinding>() {

    private val sharedViewModel: RefundVoidSharedViewModel by activityViewModels()

    private var flightSelectionAdapter: RefundFlightAdapter? = null
    override fun layoutId(): Int = R.layout.fragment_flight_selection

    private val refundQuotationResponse: RefundQuotationResponse by lazy {


        requireArguments().getParcelableCompat(
            REFUND_QUOTATION_RESPONSE,
            RefundQuotationResponse::class.java
        )!!
    }

    private val apiservice by lazy {
        ServiceGenerator.createService(FlightEndPoint::class.java)
    }

    override fun getViewModel(): BaseViewModel? = null

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            RefundFlightSelectionVMF(
                apiservice,
                AppSharedPreference.accessToken
            )
        )[RefundFlightSelectionVM::class.java]
    }


    override fun initOnCreateView() {
        bindingView.lifecycleOwner = viewLifecycleOwner
        bindingView.viewModel = viewModel
        bindingView.refundQuotation = refundQuotationResponse
        val flights = sharedViewModel.refundEligibilityResponse?.flights


        bindingView.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        bindingView.nextButton.setOnClickListener {
            refundConfirmDialog()
        }

        bindingView.nextButton.text = ButtonVRR.REFUND.value

        viewModel.selectedFlights.observe(viewLifecycleOwner) { flights ->
            sharedViewModel.selectedFlights.postValue(flights)
        }
        viewModel.refundQuotationResponse.observe(viewLifecycleOwner, EventObserver {
            if (it.status == "INITIATED") {
                viewModel.refundConfirm(it.refundSearchId)
            }
        })
        if (refundQuotationResponse.automationType) {
            bindingView.cancelRefund.visibility = View.GONE
        } else {
            bindingView.cancelRefund.visibility = View.VISIBLE
        }
        bindingView.cancelRefund.setOnClickListener {
            viewModel.cancelRefundRequest(refundQuotationResponse.refundSearchId)
        }
        viewModel.cancelRefund.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                Toast.makeText(requireContext(), "Refund Quotation cancelled", Toast.LENGTH_SHORT)
                    .show()
                for(i in 1..2){
                    findNavController().navigateUp()
                }
            }
        })
        viewModel.refundConfirmResponse.observe(viewLifecycleOwner) {
            if (it) {
                successQuotationDialog()

            }
        }
        if (!refundQuotationResponse.automationType) {
            bindingView.expiresAt.visibility = View.VISIBLE
            bindingView.remainingIcon.visibility = View.VISIBLE
            bindingView.remainingTime.visibility = View.VISIBLE
            quotationExpiresAt(refundQuotationResponse.expiresAt)
        }


        setUpFlightSelectionAdapter(flights)

        bindingView.apply {

            valueApplicationAmount.text =
                "${refundQuotationResponse.purchasePrice?.convertCurrencyToBengaliFormat() ?: 0} BDT"
            valueStConvenienceFee.text =
                "-${refundQuotationResponse.stFee?.convertCurrencyToBengaliFormat() ?: 0} BDT"
            valueAirlinePenalty.text =
                "- ${refundQuotationResponse.airlineRefundCharge?.convertCurrencyToBengaliFormat() ?: 0} BDT"
            valueTotalCharge.text =
                "- ${refundQuotationResponse.totalFee?.convertCurrencyToBengaliFormat() ?: 0} BDT"
            valueAmountToBeRefunded.text =
                "${refundQuotationResponse.totalRefundAmount?.convertCurrencyToBengaliFormat() ?: 0} BDT"
            valueDueAdjustment.text =
                "-${refundQuotationResponse.partialAdjustmentAmount?.convertCurrencyToBengaliFormat() ?: 0} BDT"
        }

        bindingView.passengers.setOnClickListener {
            findNavController().navigate(R.id.action_refundFlightSelection_to_voidPassengerInfo)
        }

        bindingView.flightDetails.setOnClickListener {
            findNavController().navigate(R.id.action_refundFlightSelection_to_voidFlightDetails)
        }
    }


    private fun successQuotationDialog(msg: String = "Your refund request has been submitted successfully. It will come into effect very soon.") {

        val commonSuccessLayoutBinding =
            CommonSuccessLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        val quotationDialog = Dialog(requireContext())
        quotationDialog.setContentView(commonSuccessLayoutBinding.root)

        commonSuccessLayoutBinding.goToHome.text = msg
        commonSuccessLayoutBinding.actualCross.setOnClickListener {
            for (i in 1..4) {
                findNavController().navigateUp()
            }
            quotationDialog.dismiss()
        }

        quotationDialog.setCanceledOnTouchOutside(false)
        quotationDialog.show()
    }


    private fun refundConfirmDialog() {
        val binding = RefundConfirmLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        val confirmDialog = Dialog(requireContext())
        confirmDialog.setContentView(binding.root)
        binding.confirmBtn.setOnClickListener() {
            viewModel.refundConfirm(refundQuotationResponse.refundSearchId)
            confirmDialog.dismiss()
        }
        binding.closeBtn.setOnClickListener() {
            confirmDialog.dismiss()
        }
        confirmDialog.setCanceledOnTouchOutside(false)
        confirmDialog.show()
    }


    private fun setUpFlightSelectionAdapter(flights: List<Flight>?) {

        flightSelectionAdapter = flights?.let {
            val isFullJourneySelect =
                sharedViewModel.refundEligibilityResponse!!.allSelectionMandatory

            sharedViewModel.selectedFlights.value?.clear()
            it.apply {
                it.forEach { flight ->
                    flight.isFlightSelected = true
                    viewModel.addSelectedFlights(flight)
                }
            }


            RefundFlightAdapter(it, isFullJourneySelect) { position, check ->
                if (check) {
                    viewModel.addSelectedFlights(flights[position])
                    flights[position].isFlightSelected = true
                } else {
                    viewModel.removeFlight(flights[position])
                    flights[position].isFlightSelected = false
                }
            }
        }

//        travellerFlightList.adapter = flightSelectionAdapter
        val mLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//        travellerFlightList.layoutManager = mLayoutManager
    }

    private fun quotationExpiresAt(expiresAt: String) {
        var timer: CountDownTimer? = null
        val currentTime = System.currentTimeMillis()
        val remainingTicktingTime =
            DateUtil.parseAPIDateTimeToMillisecond(expiresAt)
        if (remainingTicktingTime - currentTime > 0) {
            timer = object : CountDownTimer(remainingTicktingTime - currentTime, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(p0: Long) {
                    val hours = TimeUnit.MILLISECONDS.toHours(p0)
                    val day = hours / 24
                    val hour = hours % 24
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(p0) % 60
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(p0) % 60

                    bindingView.remainingTime.text =
                        "Remaining " + day + 'd' + ":$hour" + 'h' + ":$minutes" + 'm' + ":$seconds" + 's'
                }

                override fun onFinish() {
                    bindingView.expiresAt.visibility = View.GONE
                }
            }
            timer.start()
        } else {
            bindingView.expiresAt.visibility = View.GONE
        }
    }
}