package net.sharetrip.b2b.view.flight.booking.ui.proposal

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.sharetrip.b2b.databinding.FragmentProposalDetailsBinding

class IncreaseProposalBottomSheet : BottomSheetDialogFragment() {
    private lateinit var bindingView: FragmentProposalDetailsBinding
    var selectedItemPosition = -1
    val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        bindingView = FragmentProposalDetailsBinding.inflate(inflater, container, false)

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_dropdown_item_1line,
            getMarkUpParameterList()
        )
        bindingView.autoCompleteTextViewMarkUpPrice.setAdapter(adapter)

        bindingView.imageViewClose.setOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.autoCompleteTextViewMarkUpPrice.setOnItemClickListener { _, _, position, _ ->
            selectedItemPosition = position
        }

        bindingView.buttonApplyProposal.setOnClickListener {
            bundle.putInt(SELECTED_ITEM_INDEX, selectedItemPosition)
            if (bindingView.editTextAmount.text!!.isNotEmpty()) {
                bundle.putDouble(
                    SELECTED_ITEM_VALUE,
                    bindingView.editTextAmount.text.toString().toDouble()
                )
            }
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                FLIGHT_PROPOSAL_CONSTRAINTS,
                bundle
            )
            findNavController().popBackStack()
        }
        return bindingView.root
    }

    private fun getMarkUpParameterList(): ArrayList<String> {
        val markUpPriceParameterList = ArrayList<String>()
        markUpPriceParameterList.add(ProposalConstraintsEnum.FIXED_ON_BASE_FARE.constraintsType)
        markUpPriceParameterList.add(ProposalConstraintsEnum.PERCENTAGE_ON_BASE_FARE.constraintsType)
        markUpPriceParameterList.add(ProposalConstraintsEnum.PERCENTAGE_ON_TOTAL_TAX.constraintsType)
        return markUpPriceParameterList
    }

    companion object {
        const val FLIGHT_PROPOSAL_CONSTRAINTS = "Selected Item Index"
        const val SELECTED_ITEM_INDEX = "Selected Item Index"
        const val SELECTED_ITEM_VALUE = "Selected Item Value"
    }
}
