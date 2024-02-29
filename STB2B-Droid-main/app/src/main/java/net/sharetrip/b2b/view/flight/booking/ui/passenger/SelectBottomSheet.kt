package net.sharetrip.b2b.view.flight.booking.ui.passenger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import net.sharetrip.b2b.databinding.BottomSheetSelectBinding
import net.sharetrip.b2b.util.MsgUtils
import net.sharetrip.b2b.util.setNavigationResult
import net.sharetrip.b2b.view.flight.booking.model.SpecialServiceRequest
import net.sharetrip.b2b.view.flight.booking.ui.ListItemClickListener

class SelectBottomSheet : BottomSheetDialogFragment(), ListItemClickListener<SpecialServiceRequest> {
    private lateinit var navigationResultKey: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = BottomSheetSelectBinding.inflate(inflater, container, false)
        val title = arguments?.getString(ARG_BOTTOM_SHEET_TITLE, MsgUtils.bottomSheetDefaultTitle)
        navigationResultKey = arguments?.getString(ARG_NAVIGATION_RESULT_KEY) ?: ""
        bindingView.texTitle.text = title

        bindingView.texTitle.setOnClickListener {
            findNavController().navigateUp()
        }

        val mealList = if (navigationResultKey == KEY_MEAL)
            generateMealList()
        else
            generateChairList()

        val adapter = SelectAdapter(mealList, mealList[0].code, this)
        bindingView.listOption.adapter = adapter
        return bindingView.root
    }

    private fun generateMealList(): List<SpecialServiceRequest> {
        val moshi = Moshi.Builder()
            .build()

        val listType = Types.newParameterizedType(List::class.java, SpecialServiceRequest::class.java)
        val adapter: JsonAdapter<List<SpecialServiceRequest>> = moshi.adapter(listType)

        val file = "meal.json"
        val jsonStr = requireContext().assets.open(file).bufferedReader().use{ it.readText()}
        val list = adapter.fromJson(jsonStr)

        list?.let {
            return list
        }
        return emptyList()
    }

    private fun generateChairList() : List<SpecialServiceRequest> {
        val moshi = Moshi.Builder()
            .build()

        val listType = Types.newParameterizedType(List::class.java, SpecialServiceRequest::class.java)
        val adapter: JsonAdapter<List<SpecialServiceRequest>> = moshi.adapter(listType)

        val file = "chair.json"
        val jsonStr = requireContext().assets.open(file).bufferedReader().use{ it.readText()}
        val list = adapter.fromJson(jsonStr)

        list?.let {
            return list
        }
        return emptyList()
    }

    override fun onClickItem(data: SpecialServiceRequest) {
        setNavigationResult(data, navigationResultKey)
        findNavController().popBackStack()
    }
}

const val ARG_BOTTOM_SHEET_TITLE = "bottom_sheet_title"
const val ARG_NAVIGATION_RESULT_KEY = "navigation_result_key"
