package net.sharetrip.b2b.view.transaction.view.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.BottomSheetTransactionFilterBinding


class TransactionFilterBottomSheet : BottomSheetDialogFragment(),
    TransactionFilterAdapter.ChangedFilterOnItemClick {

    private lateinit var bindingView: BottomSheetTransactionFilterBinding
    private var index = 0
    lateinit var adapter: TransactionFilterAdapter
    lateinit var datalist: List<String>
    var selectedData = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView = BottomSheetTransactionFilterBinding.inflate(inflater, container, false)
        index =
            arguments?.getInt(TransactionListFilterFragment.ARG_TRANSACTION_FILTER_TITLE, 0)!!
        datalist =
            arguments?.getStringArrayList(TransactionListFilterFragment.ARG_TRANSACTION_FILTER_DATA_LIST)!!
        adapter = TransactionFilterAdapter(datalist, this)
        bindingView.recyclerFilter.adapter = adapter

        when (index) {
            TransactionFilterTypeEnum.PAYMENT_TYPE.filterCode -> {
                bindingView.textViewTitle.text = requireContext().getString(R.string.payment_type)
            }

            TransactionFilterTypeEnum.SOURCE.filterCode -> {
                bindingView.textViewTitle.text = requireContext().getString(R.string.source)
            }

            TransactionFilterTypeEnum.STATUS.filterCode -> {
                bindingView.textViewTitle.text = requireContext().getString(R.string.status)
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
        selectedData = if (data == getString(R.string.hotel) || data == getString(R.string.flight) || data == getString(R.string.payment)) {
            data.toUpperCase()
        } else {
            data
        }
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

    companion object {
        const val ARGUMENT_CHECKED_DATA = "ARGUMENT_CHECKED_CODE"
        const val RESULT_CHECKED_DATA = "ARGUMENT_RESULT_CODE"
    }
}