package net.sharetrip.b2b.view.flight.history.reissue_change_date.manualquotation

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import net.sharetrip.b2b.R
import net.sharetrip.b2b.base.BaseViewModel
import net.sharetrip.b2b.databinding.CommonSuccessLayoutBinding
import net.sharetrip.b2b.databinding.ReissueManualQuotationBinding
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.AppSharedPreference
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ReissueChangeDateFragment
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ReissueChangeDateSharedViewModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.flightdetails.ReissueFlightDetailsFragment.Companion.QUOTATION_PARAM
import net.sharetrip.b2b.view.flight.history.reissue_change_date.flightdetails.ReissueFlightDetailsFragment.Companion.REISSUE_SEARCH_ID
import net.sharetrip.b2b.view.flight.history.reissue_change_date.flightdetails.ReissueFlightDetailsFragment.Companion.SEQUENCE_CODE
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.FlightX
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlight
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.Segment
import net.sharetrip.b2b.view.flight.history.reissue_change_date.network.ReissueApiService
import net.sharetrip.b2b.widgets.BaseFragment

class ReissueManualQuotationFragment : BaseFragment<ReissueManualQuotationBinding>() {

    private val sharedViewModel: ReissueChangeDateSharedViewModel by activityViewModels()
    private lateinit var quotationDialog: Dialog
    private lateinit var reissueSearchID: String
    private lateinit var sequenceCode: String
    override fun layoutId(): Int {
        return R.layout.reissue_manual_quotation
    }

    override fun getViewModel(): BaseViewModel? {
        return null
    }

    private val reissueApiService by lazy {
        ServiceGenerator.createService(ReissueApiService::class.java)
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            QuotationVMF(
                reissueApiService,
                AppSharedPreference.accessToken
            )
        )[QuotationVM::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reissueSearchID =
            arguments?.getBundle(QUOTATION_PARAM)?.getString(REISSUE_SEARCH_ID).toString()
        sequenceCode = arguments?.getBundle(QUOTATION_PARAM)?.getString(SEQUENCE_CODE).toString()
    }

    override fun initOnCreateView() {

        bindingView.lifecycleOwner = viewLifecycleOwner

        bindingView.sharedVM = sharedViewModel
        bindingView.viewModel = viewModel


        val tempList = createBeforeAfterList()
        Log.d(ReissueChangeDateFragment.TAG, "TempList.size = ${tempList.size}")
        val mAdapter = ReissueManualAdapter(
            tempList,
            sharedViewModel.totalChanges,
            sharedViewModel.flightNoBefore
        )
        bindingView.itineraryRecyclerView.adapter = mAdapter
        bindingView.itineraryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        bindingView.quotationBtn.setOnClickListener {
            if (sharedViewModel.reissueEligibilityResponse?.automationSupported == true) {
                agreeAndConfirmDialog()
            } else {
                viewModel.onClickQuotationSelectApi(
                    sharedViewModel.bookingCode,
                    reissueSearchID,
                    sequenceCode
                )
            }
        }

        bindingView.quotationBtn.isEnabled = !viewModel.isLoading.get()

        viewModel.quotationSuccess.observe(viewLifecycleOwner) {
            if (it) {
                sharedViewModel.quotationConfirmCheck.postValue(true)
            }
        }

        viewModel.checkReissueConfirm.observe(viewLifecycleOwner) {
            if (it) {
                sharedViewModel.reissueSuccess.postValue(true)
                successQuotationDialog(msg = reissueSuccess)
            }
        }

        viewModel.reissueQuotationRequestSelectResponse.observe(
            viewLifecycleOwner,
            EventObserver { someResponse ->
                successQuotationDialog(msg = someResponse?.response?.message ?: defaultMsg)

            })

//        bindingView.cancelBtn.setOnClickListener {
//            viewModel.onclickReissueManualCancel(reissueSearchID)
//        }

    }

    private fun agreeAndConfirmDialog() {
        quotationDialog = Dialog(requireContext())
        quotationDialog.setContentView(R.layout.agree_and_confirm_layout)
        val agreeAndConfirm = quotationDialog.findViewById<Button>(R.id.agreeBtn)
        val cancel = quotationDialog.findViewById<Button>(R.id.closeBtn)
        agreeAndConfirm.setOnClickListener() {
            viewModel.onclickReissueConfirm(
                sharedViewModel.reissueSearchId,
                sharedViewModel.reissueSequenceCode
            )
            quotationDialog.dismiss()
        }
        cancel.setOnClickListener() {
            quotationDialog.dismiss()
        }
        quotationDialog.setCanceledOnTouchOutside(false)
        quotationDialog.show()
    }

    private fun successQuotationDialog(msg: String = defaultMsg) {

        val commonSuccessLayoutBinding =
            CommonSuccessLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        quotationDialog = Dialog(requireContext())
        quotationDialog.setContentView(commonSuccessLayoutBinding.root)

        commonSuccessLayoutBinding.goToHome.text = msg
        commonSuccessLayoutBinding.actualCross.setOnClickListener {
            for (i in 1..7) {
                findNavController().navigateUp()
            }
            quotationDialog.dismiss()
        }

        quotationDialog.setCanceledOnTouchOutside(false)
        quotationDialog.show()
    }


    private val defaultMsg: String by lazy {
        requireContext().getString(R.string.your_reissue_quotation_request_has_been_submitted_successfully_it_will_come_into_effect_very_soon)
    }

    private val reissueSuccess: String by lazy {
        requireContext().getString(R.string.your_reissue_request_has_been_submitted_successfully_it_will_come_into_effect_very_soon)
    }

    private fun createBeforeAfterList(): List<Pair<ReissueFlight, Segment>> {
        val after: FlightX = sharedViewModel.flightToBeBooked!!
        var before = sharedViewModel.manualFlights

        val size = sharedViewModel.manualFlights?.size
        sharedViewModel.totalChanges = size
        val outputList = ArrayList<Pair<ReissueFlight, Segment>>()
        if (size != 0) {
            for (i in 0 until after.segments.size) {
                if (before[i].departure?.dateTime != after.segments[i].departureDateTime.date) {
                }
                outputList.add(Pair(before[i], after.segments[i]))
            }
        }
        return outputList
    }
}