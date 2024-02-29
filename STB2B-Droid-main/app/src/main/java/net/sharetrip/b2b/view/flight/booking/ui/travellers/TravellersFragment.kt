package net.sharetrip.b2b.view.flight.booking.ui.travellers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentTravelersBinding
import net.sharetrip.b2b.util.*

class TravellersFragment : Fragment() {
    private lateinit var childDobAdapter: ChildDOBAdapter
    private lateinit var travellersClassAdapter: TravellersClassAdapter

    private val viewModel: TravellersVM by lazy {
        TravellersVMFactory(
            arguments,
        ).create(TravellersVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentTravelersBinding.inflate(inflater, container, false)

        bindingView.travellersVM = viewModel;

        val travellersClass = listOf(
            getString(R.string.economy),
            getString(R.string.premium_economy),
            getString(R.string.business),
            getString(R.string.first_class)
        )

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        travellersClassAdapter = TravellersClassAdapter(travellersClass, viewModel)
        bindingView.recyclerTravelClass.adapter = travellersClassAdapter;

        childDobAdapter = ChildDOBAdapter(arguments?.getString(FLIGHT_DATE)?.let { toList(it) }!!,viewModel)
        bindingView.recyclerBirthDate.adapter = childDobAdapter;

        viewModel.childDOBNumber.observe(viewLifecycleOwner) {
            childDobAdapter.updateData(it)
        }

        viewModel.showMessage.observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.fill_children_date_of_birth),
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.moveToBack.observe(viewLifecycleOwner, EventObserver {
            val navKey = when (arguments?.getString(TRIP_TYPE)) {
                ONE_WAY -> TRAVELLERS_INFO_ONE_WAY
                RETURN -> TRAVELLERS_INFO_RETURN
                OTHER -> TRAVELLERS_INFO_MULTI_CITY
                else -> TRAVELLERS_INFO_ONE_WAY
            }
            setNavigationResult(it, navKey)
            findNavController().navigateUp()
        })

        return bindingView.root
    }
}

const val TRAVELLERS_INFO_ONE_WAY = "travellers_info_one_way "
const val TRAVELLERS_INFO_RETURN = "travellers_info_return"
const val TRAVELLERS_INFO_MULTI_CITY = "travellers_info_multi_city"
const val TRAVELLERS_INFO = "travellers_info"
const val FLIGHT_DATE = "flight_date"
