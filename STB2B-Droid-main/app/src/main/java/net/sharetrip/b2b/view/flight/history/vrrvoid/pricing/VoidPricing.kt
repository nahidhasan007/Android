package net.sharetrip.b2b.view.flight.history.vrrvoid.pricing

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.databinding.CommonSuccessLayoutBinding
import net.sharetrip.b2b.databinding.RefundConfirmLayoutBinding
import net.sharetrip.b2b.databinding.RefundConfrimFragmentBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.util.convertCurrencyToBengaliFormat
import net.sharetrip.b2b.view.flight.history.model.ButtonVRR
import net.sharetrip.b2b.view.flight.history.model.VRRQuotationResponse
import net.sharetrip.b2b.view.flight.history.refund.RefundVoidSharedViewModel
import net.sharetrip.b2b.widgets.BaseFragment

class VoidPricing : BaseFragment<RefundConfrimFragmentBinding>() {

    private val sharedVM: RefundVoidSharedViewModel by activityViewModels()
    override fun layoutId(): Int = R.layout.refund_confrim_fragment

    private lateinit var voidPricing: VRRQuotationResponse

    override fun getViewModel(): BaseViewModel? = null

    private val apiService by lazy {
        ServiceGenerator.createService(FlightEndPoint::class.java)
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            VoidPricingVMF(
                AppSharedPreference.accessToken,
                apiService
            )
        )[VoidPricingViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        voidPricing = arguments?.getParcelable("void_pricing")!!
    }

    @SuppressLint("SetTextI18n")
    override fun initOnCreateView() {

        bindingView.lifecycleOwner = viewLifecycleOwner
        bindingView.vm = viewModel

        bindingView.priceTextViewSubtotal.text = "${voidPricing.purchasePrice.toLong().convertCurrencyToBengaliFormat()} BDT"
        bindingView.textViewAdvanceIncomeTaxCost.text = "-${voidPricing.airlineVoidCharge} BDT"
        bindingView.textViewConvenienceCost.text = "-${voidPricing.stVoidCharge} BDT"
        bindingView.totalRefundChargeAmountTextView.text = "-${voidPricing.totalFee} BDT"
        bindingView.priceTextViewTotal.text = "${voidPricing.totalReturnAmount?.toLong()?.convertCurrencyToBengaliFormat()} BDT"

        bindingView.refundBtn.text = ButtonVRR.VOID.value

        bindingView.refundBtn.setOnClickListener {
            voidPricing.voidSearchId.let { voidSearchId -> voidConfirmDialog(voidSearchId) }
        }

        viewModel.voidConfirmMsg.observe(viewLifecycleOwner, EventObserver {
            successQuotationDialog("Your void quotation request has been processed successfully.")
        })
        viewModel.voidCancelMsg.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                for (i in 1..2) {
                    findNavController().navigateUp()
                }
            }
        })
//        if(viewModel.isLoading.get()){
//            bindingView.progressBar.visibility = View.VISIBLE
//            bindingView.refundBtn.isEnabled = false
//        }
//        else {
//            bindingView.progressBar.visibility = View.GONE
//            bindingView.refundBtn.isEnabled = false
//        }

        bindingView.Passenger.setOnClickListener {
            findNavController().navigate(R.id.action_voidPricing_to_voidPassengerInfo)
        }
        bindingView.flightDetails.setOnClickListener {
            findNavController().navigate(R.id.action_voidPricing_to_voidFlightDetails)
        }

        bindingView.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun voidConfirmDialog(voidSearchId: String) {
        val binding = RefundConfirmLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        val confirmDialog = Dialog(requireContext())
        confirmDialog.setContentView(binding.root)
        binding.confirmBtn.setOnClickListener() {
            viewModel.confirmVoid(voidSearchId)
            confirmDialog.dismiss()
        }
        binding.closeBtn.setOnClickListener() {
            confirmDialog.dismiss()
        }
        confirmDialog.setCanceledOnTouchOutside(false)
        confirmDialog.show()
    }

    private fun successQuotationDialog(msg: String) {

        val commonSuccessLayoutBinding =
            CommonSuccessLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        val quotationDialog = Dialog(requireContext())
        quotationDialog.setContentView(commonSuccessLayoutBinding.root)

        commonSuccessLayoutBinding.goToHome.text = msg
        commonSuccessLayoutBinding.actualCross.setOnClickListener {
            for(i in 1..2){
                findNavController().navigateUp()
            }
            quotationDialog.dismiss()
        }

        quotationDialog.setCanceledOnTouchOutside(false)
        quotationDialog.show()
    }
}