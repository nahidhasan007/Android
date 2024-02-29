package net.sharetrip.b2b.view.flight.history.reissue_change_date.travellernumber

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.databinding.FragmentReissueCabinBinding
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.util.setNavigationResult
import net.sharetrip.b2b.view.flight.booking.model.TravellersInfo
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.TravellerClassType
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.ARG_TRAVELLER
import net.sharetrip.b2b.view.flight.history.reissue_change_date.utils.*

class TravellerNumberFragment: Fragment() {
    companion object {
        const val   TAG = "TravellerNumber"
    }

    private var _bindingView: FragmentReissueCabinBinding? = null
    private val bindingView: FragmentReissueCabinBinding get() = _bindingView!!

    private lateinit var travellersClassAdapter: TravellersClassAdapter
    private val viewModel by lazy {
        val travellerInfo: TravellersInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_TRAVELLER, TravellersInfo::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ARG_TRAVELLER)!!
        }
        TravellerNumberViewModelFactory(
            travellerInfo!!
        ).create(TravellerNumberViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingView = FragmentReissueCabinBinding.inflate(inflater, container, false)
        bindingView.root.setOnClickListener {  }
        return bindingView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        travellersClassAdapter = TravellersClassAdapter(getTravellerClassList(), viewModel)

        viewModel.showMessage.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })


        bindingView.viewModel = viewModel
        bindingView.recyclerTravelClass.adapter = travellersClassAdapter

        bindingView.buttonDone.setOnClickListener {
            if (viewModel.checkDob()) {
                val intent = Intent()
                intent.putExtra(EXTRA_NUMBER_OF_ADULT, viewModel.numberOfAdult)
                intent.putExtra(EXTRA_NUMBER_OF_CHILDREN, viewModel.numberOfChildren)
                intent.putExtra(EXTRA_NUMBER_OF_INFANT, viewModel.numberOfInfant)
                intent.putExtra(EXTRA_TRIP_CLASS_TYPE, viewModel.tripClassType)
                intent.putParcelableArrayListExtra(EXTRA_CHILD_DOB_LIST, viewModel.childDobList)
                setNavigationResult(intent, ARG_TRAVELLER)
                findNavController().navigateUp()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingView = null
    }


    private fun getTravellerClassList(): List<String> {
        return listOf(
            TravellerClassType.ECONOMY.type,
            TravellerClassType.PREMIUM_ECONOMY.type,
            TravellerClassType.BUSINESS.type,
            TravellerClassType.FIRST_CLASS.type
        )
    }

}