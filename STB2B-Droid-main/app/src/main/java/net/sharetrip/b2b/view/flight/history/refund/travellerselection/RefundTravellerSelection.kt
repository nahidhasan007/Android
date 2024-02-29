package net.sharetrip.b2b.view.flight.history.refund.travellerselection

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import net.sharetrip.b2b.R
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.databinding.CommonSuccessLayoutBinding
import net.sharetrip.b2b.databinding.RefundConfirmLayoutBinding
import net.sharetrip.b2b.databinding.RefundTravellerSelectionBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.util.DebouncedOnClickListener
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.view.flight.history.historydetails.FlightHistoryDetailsFragment.Companion.ARG_REFUND_ELIGIBILITY_RESPONSE
import net.sharetrip.b2b.view.flight.history.model.RefundEligibleTravellerResponse
import net.sharetrip.b2b.view.flight.history.refund.RefundVoidSharedViewModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.getParcelableCompat
import net.sharetrip.b2b.widgets.BaseFragment

class RefundTravellerSelection : BaseFragment<RefundTravellerSelectionBinding>() {

    companion object {
        const val TAG = "RefundTravellerSelection"
        const val REFUND_QUOTATION_RESPONSE = "REFUND_QUOTATION_RESPONSE"
    }
    override fun layoutId(): Int = R.layout.refund_traveller_selection

    val sharedViewModel: RefundVoidSharedViewModel by activityViewModels()

    override fun getViewModel(): BaseViewModel? = null

    private var travellerListAdapter: TravellerAdapter? = null
    private lateinit var refundEligibleTravellerResponse: RefundEligibleTravellerResponse

    private val apiService by lazy {
        ServiceGenerator.createService(FlightEndPoint::class.java)
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            RefundTravellerSelectionVMF(
                AppSharedPreference.accessToken,
                apiService
            )
        )[RefundTravellerSelectionVM::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refundEligibleTravellerResponse =
            requireArguments().getParcelableCompat(ARG_REFUND_ELIGIBILITY_RESPONSE, RefundEligibleTravellerResponse::class.java)!!
        sharedViewModel.refundEligibilityResponse = refundEligibleTravellerResponse

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val travellers = sharedViewModel.refundEligibilityResponse?.travellers
        if (travellers != null) {
            setupTravellerListAdapter(travellers)
        }

        val flightContainerAdapter by lazy {
            refundEligibleTravellerResponse.flights?.let { FlightContainerAdapter(it) }
        }

        bindingView.flightsContainer.adapter = flightContainerAdapter
        bindingView.flightsContainer.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun initOnCreateView() {
        initObservers()

        if(refundEligibleTravellerResponse.automationType) {
            bindingView.nextButton.setText(R.string.next_btn)
        } else {
            bindingView.nextButton.setText(R.string.get_quotation)
        }

        bindingView.toolbar.title = "Choose Traveller"
        bindingView.flightsText.text = getString(R.string.refundable_flights)

        viewModel.selectedPassengers.observe(viewLifecycleOwner){
            if(sharedViewModel.refundEligibilityResponse?.travellers?.size!! >1){
                bindingView.selectALL.visibility = View.VISIBLE
            }
            else {
                bindingView.selectALL.visibility = View.GONE
            }
        }

        bindingView.selectALL.setOnClickListener{
            travellerListAdapter?.getDataSet()?.forEach {
                it.isChecked = true
            }

            travellerListAdapter?.notifyDataSetChanged()
        }

        bindingView.nextButton.setOnClickListener(object : DebouncedOnClickListener() {
            override fun onDebouncedClick(v: View?) {
                if(refundEligibleTravellerResponse.automationType) {
                    executeRefundQuotation()
                } else {
                   // todo: show manual refund popup
                    confirmManualQuotationDialog()
                }
        }})

        bindingView.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun confirmManualQuotationDialog() {
        val binding = RefundConfirmLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        val confirmDialog = Dialog(requireContext())
        confirmDialog.setContentView(binding.root)

        binding.quotationConfirm.text = "Request Refund Quotation for this Flight"
        binding.quotationConfirmBody.text = "Automatic Refund is not supported in this flight, corresponding flight team will generate a manual Refund quotation for you."

        binding.confirmBtn.setOnClickListener() {
            executeRefundQuotation()
            confirmDialog.dismiss()
        }
        binding.closeBtn.setOnClickListener() {
            confirmDialog.dismiss()
        }
        confirmDialog.setCanceledOnTouchOutside(false)
        confirmDialog.show()
    }


    fun executeRefundQuotation() {
        val eTickets = arrayListOf<String>()
        val travellers = viewModel.selectedPassengers.value?: emptyList()

        sharedViewModel.voidSelectedTraveller.postValue(ArrayList(travellers))

        travellers.forEach { traveller ->
            traveller.eTicket?.let { eTicket -> eTickets.add("\"" + eTicket + "\"") }
        }

        viewModel.refundQuotation(
            eTickets.toString(),
            refundEligibleTravellerResponse.bookingCode
        )

    }

    private fun initObservers() {
        viewModel.liveRefundQuotationResponse.observe(viewLifecycleOwner, EventObserver { refundQuotationResponse ->
            if(refundEligibleTravellerResponse.automationType) {
                val args = Bundle()
                args.putParcelable(REFUND_QUOTATION_RESPONSE, refundQuotationResponse)

                findNavController().navigate(
                    R.id.action_refundTravellerSelection_to_refundFlightSelection, args
                )
            } else {
                if(refundQuotationResponse.status=="INITIATED"){
                    viewModel.refundConfirm(refundQuotationResponse.refundSearchId)
                }
            }
        })

        viewModel.selectedPassengers.observe(viewLifecycleOwner){ travellers->
            bindingView.nextButton.isEnabled = travellers.size != 0
        }



        viewModel.refundConfirmResponse.observe(viewLifecycleOwner){
            if(it){
                successQuotationDialog()

            }
        }

    }

    private fun successQuotationDialog(msg: String = "Your refund request has been submitted successfully. It will come into effect very soon.") {

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


    private fun setupTravellerListAdapter(travellers: List<ReissueTraveller>) {
//        val isPaxSelectionType =
//            TravellerSelectionFragment.PaxSelectionType.SINGLE
        // sharedViewModel.reissueEligibilityResponse?.paxSelectionType  // property does not exist!
        if (travellers.isNotEmpty()) {
            travellerListAdapter =
                TravellerAdapter(
                    travellers,
                ) { position, check ->
                    if (check) {
                        viewModel.addSelectedTraveller(travellers[position])
                        travellers[position].isChecked = true
                    } else {
                        viewModel.removeTraveller(travellers[position])
                        travellers[position].isChecked = false
                    }
                }
//            requireView().findViewById<RecyclerView>(R.id.listTraveller).adapter = travellerListAdapter
//            requireView().findViewById<RecyclerView>(R.id.listTraveller).layoutManager = LinearLayoutManager(requireContext())
            bindingView.listTraveller.adapter = travellerListAdapter
            bindingView.listTraveller.layoutManager = LinearLayoutManager(requireContext())
        } else {
            Log.e("EmptyList", "Traveller list is empty()")
        }
    }


}