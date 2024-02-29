package net.sharetrip.b2b.view.flight.history.reissue_change_date.travellerselection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import net.sharetrip.b2b.databinding.FragmentTravellerSelectionBinding
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ReissueChangeDateSharedViewModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueTraveller

class TravellerSelectionFragment : Fragment() {
    companion object {
        const val TAG = "TravellerSelectionFragment"
    }

    private val sharedViewModel: ReissueChangeDateSharedViewModel by activityViewModels()
    private var _binding: FragmentTravellerSelectionBinding? = null
    private val binding: FragmentTravellerSelectionBinding get() = _binding!!
    private var travellerListAdapter: TravellerListAdapter? = null
    private var travellerItemAdapter: TravellerItemAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravellerSelectionBinding.inflate(inflater, container, false)
        binding.root.setOnClickListener { }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectALL.setOnClickListener {
            sharedViewModel.isALLSelected.postValue(true)
            travellerListAdapter?.getDataSet()?.forEach {
                it.isChecked = true
            }

            travellerListAdapter?.notifyDataSetChanged()
        }

        val travellers = sharedViewModel.reissueEligibilityResponse?.travellers
        if (travellers != null) {
            travellerItemAdapter = TravellerItemAdapter(travellers)
            binding.listTravellerInfo.adapter = travellerItemAdapter
            setupTravellerListAdapter(travellers)
        }

        sharedViewModel.reissueEligibilityResponse?.paxSelectionMsg?.let {
            binding.paxSelectionMsg.text = it
        }
        if (sharedViewModel.reissueEligibilityResponse?.reissueSearch?.status != "QUOTED") {
            if (sharedViewModel.reissueEligibilityResponse?.travellers?.size!! > 1) {
                binding.selectALL.visibility = View.VISIBLE
            } else {
                binding.selectALL.visibility = View.GONE
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setupTravellerListAdapter(travellers: List<ReissueTraveller>) {
        val isPaxSelectionType =
            PaxSelectionType.SINGLE // sharedViewModel.reissueEligibilityResponse?.paxSelectionType  // property does not exist!
        travellerListAdapter =
            TravellerListAdapter(
                travellers,
                true // isPaxSelectionType == PaxSelectionType.ALL.type
            ) { position, check ->
                // todo: logic
                /*
                if (isPaxSelectionType == PaxSelectionType.SINGLE.type && sharedViewModel.selectedPassengers.value?.size == 1) {
                    Toast.makeText(
                        requireContext(),
                        "You cannot select more than one traveller at a time!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // implemented below
                }
                */
                if (check) {
                    sharedViewModel.addSelectedTraveller(travellers[position])
                    travellers[position].isChecked = true
                } else {
                    sharedViewModel.removeTraveller(travellers[position])
                    travellers[position].isChecked = false
                }
            }
        binding.listTraveller.adapter = travellerListAdapter
        binding.listTraveller.layoutManager = LinearLayoutManager(requireContext())
    }

    enum class PaxSelectionType(val type: String) { ALL("ALL"), MULTIPLE("MULTIPLE"), SINGLE("SINGLE") }
}
