package net.sharetrip.b2b.view.flight.history.vrrvoid.travellerselection

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import net.sharetrip.b2b.R
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.databinding.RefundConfirmLayoutBinding
import net.sharetrip.b2b.databinding.RefundTravellerSelectionBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.view.flight.history.historydetails.FlightHistoryDetailsFragment
import net.sharetrip.b2b.view.flight.history.model.RefundEligibleTravellerResponse
import net.sharetrip.b2b.view.flight.history.model.VRRQuotationResponse
import net.sharetrip.b2b.view.flight.history.refund.RefundVoidSharedViewModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller
import net.sharetrip.b2b.widgets.BaseFragment

class TravellerSelection : BaseFragment<RefundTravellerSelectionBinding>() {

    val sharedViewModel: RefundVoidSharedViewModel by activityViewModels()
    override fun layoutId(): Int = R.layout.refund_traveller_selection

    override fun getViewModel(): BaseViewModel? = null

    private var travellerListAdapter: VoidTravellerAdapter? = null
    private var flightSegmentAdapter: FlightSegmentAdapter? = null
    private lateinit var voidEligibleTravellerResponse: RefundEligibleTravellerResponse

    private val apiService by lazy {
        ServiceGenerator.createService(FlightEndPoint::class.java)
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            TravellerSelectionVMF(
                AppSharedPreference.accessToken,
                apiService
            )
        )[TravellerSelectionVM::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        voidEligibleTravellerResponse =
            arguments?.getParcelable(FlightHistoryDetailsFragment.ARG_VOID_ELIGIBILITY_RESPONSE)!!
        sharedViewModel.voidEligibilityResponse = voidEligibleTravellerResponse

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val travellers = sharedViewModel.voidEligibilityResponse?.travellers
        if (travellers != null) {
            setupTravellerListAdapter(travellers)
        }
        if (sharedViewModel.flightHistory?.segments != null) {
            val segments = sharedViewModel.flightHistory?.segments
            flightSegmentAdapter = segments?.let { FlightSegmentAdapter(it) }
            bindingView.flightsContainer.adapter = flightSegmentAdapter
        }
    }

    override fun initOnCreateView() {
//        sharedViewModel.title.postValue("Select Traveller")
        bindingView.lifecycleOwner = viewLifecycleOwner
//        bindingView.sharedVm = sharedViewModel
//        bindingView.toolbar.title = sharedViewModel.title.value
        bindingView.selectALL.setOnClickListener {
            travellerListAdapter?.getDataSet()?.forEach {
                it.isChecked = true
            }

            travellerListAdapter?.notifyDataSetChanged()
        }
        bindingView.paxSelectionMsg.text = getString(R.string.select_void_traveller_content)
        viewModel.selectedPassengers.observe(viewLifecycleOwner) { travellers ->
            if (travellers.size < 1) {
                bindingView.nextButton.isEnabled = false
            } else {
                bindingView.nextButton.isEnabled = true
                sharedViewModel.voidSelectedTraveller.postValue(travellers)
            }
        }
        if (sharedViewModel.voidEligibilityResponse?.travellers?.size!! > 1) {
            bindingView.selectALL.visibility = View.VISIBLE
        } else {
            bindingView.selectALL.visibility = View.GONE
        }
        bindingView.nextButton.setOnClickListener {

            var eTickets = arrayListOf<String>()

            viewModel.selectedPassengers.observe(viewLifecycleOwner) { travellers ->
                sharedViewModel.selectedPassengers.value = travellers
                travellers.forEach { traveller ->
                    traveller.eTicket?.let { eticket -> eTickets.add("\"" + eticket + "\"") }
                }

            }
            viewModel.getVoidQuotation(
                eTickets.toString(),
                voidEligibleTravellerResponse.bookingCode
            )
        }
        if(!voidEligibleTravellerResponse.automationType){
            bindingView.nextButton.text = getString(R.string.get_quotation)
        }
        viewModel.voidQuotationResponse.observe(viewLifecycleOwner, EventObserver {
            if (it != null && sharedViewModel.voidEligibilityResponse?.automationType == true) {
                findNavController().navigate(
                    R.id.action_travellerSelection_to_voidPricing,
                    bundleOf("void_pricing" to it)
                )
            } else {
//                viewModel.confirmVoid(it.voidSearchId)
                confirmManualQuotationDialog(it)
            }
        })
        viewModel.voidStatus.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.vrr_request),
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigateUp()
            }
        }
        bindingView.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupTravellerListAdapter(travellers: List<ReissueTraveller>) {
//        val isPaxSelectionType =
//            TravellerSelectionFragment.PaxSelectionType.SINGLE
        // sharedViewModel.reissueEligibilityResponse?.paxSelectionType  // property does not exist!
        if (travellers.isNotEmpty()) {
            travellerListAdapter =
                VoidTravellerAdapter(
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

    private fun confirmManualQuotationDialog(quotation: VRRQuotationResponse) {
        val binding = RefundConfirmLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        val confirmDialog = Dialog(requireContext())
        confirmDialog.setContentView(binding.root)

        binding.quotationConfirm.text = getString(R.string.void_quotation)
        binding.quotationConfirmBody.text = quotation.msg

        binding.confirmBtn.setOnClickListener() {
            viewModel.confirmVoid(quotation.voidSearchId)
            confirmDialog.dismiss()
        }
        binding.closeBtn.setOnClickListener() {
            confirmDialog.dismiss()
        }
        confirmDialog.setCanceledOnTouchOutside(false)
        confirmDialog.show()
    }
}