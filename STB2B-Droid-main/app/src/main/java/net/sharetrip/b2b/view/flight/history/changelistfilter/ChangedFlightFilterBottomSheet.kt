package net.sharetrip.b2b.view.flight.history.changelistfilter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.BottomSheetChangedFlightFilterBinding

class ChangedFlightFilterBottomSheet : BottomSheetDialogFragment(),
    ChangeRequestFilterAdapter.ChangedFilterOnItemClick {

    private lateinit var bindingView: BottomSheetChangedFlightFilterBinding
    private var index = 0
    lateinit var adapter: ChangeRequestFilterAdapter
    lateinit var datalist: List<String>
    var selectedData = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView = BottomSheetChangedFlightFilterBinding.inflate(inflater, container, false)
        index =
            arguments?.getInt(ChangeTicketListFilterFragment.ARG_CHANGED_FLIGHT_FILTER_TITLE, 0)!!
        datalist =
            arguments?.getStringArrayList(ChangeTicketListFilterFragment.ARG_CHANGED_FLIGHT_FILTER_DATA_LIST)!!
        adapter = ChangeRequestFilterAdapter(datalist, this)
        bindingView.recyclerFilter.adapter = adapter

        when (index) {
            ChangedFlightFilterTypeEnum.REQUEST_TYPE.filterCode -> {
                bindingView.textViewTitle.text = requireContext().getString(R.string.request_type)
            }

            ChangedFlightFilterTypeEnum.REQUEST_STATUS.filterCode -> {
                bindingView.textViewTitle.text = requireContext().getString(R.string.request_status)
            }
        }

        bindingView.imageViewClose.setOnClickListener {
            dismiss()
        }

        bindingView.buttonFilterApply.setOnClickListener {
            setDataForResult(it)
            dismiss()
        }

        return bindingView.root
    }

    override fun selectRequest(data: String) {
        selectedData = data
    }

    private fun setDataForResult(view: View) {
        val bundle = Bundle()
        bundle.putString(ARGUMENT_CHECKED_DATA, selectedData)
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            RESULT_CHECKED_DATA,
            bundle
        )
        findNavController().popBackStack()
    }

    companion object{
        const val ARGUMENT_CHECKED_DATA = "ARGUMENT_CHECKED_CODE"
        const val RESULT_CHECKED_DATA = "ARGUMENT_RESULT_CODE"
    }
}