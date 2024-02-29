package net.sharetrip.b2b.view.flight.history.reissue_change_date.flightselection

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.FragmentReissueFlightSelectionBinding
import net.sharetrip.b2b.view.flight.history.reissue_change_date.ReissueChangeDateSharedViewModel
import net.sharetrip.b2b.view.flight.history.reissue_change_date.models.ReissueFlight

class ReissueFlightSelectionFragment: Fragment() {
    companion object {
        const val TAG: String = "ReissueFlightSelectFrag"
    }

    private val sharedViewModel: ReissueChangeDateSharedViewModel by activityViewModels()
    private var flightSelectionAdapter: FlightSelectionAdapter? = null
    private var _bindingView: FragmentReissueFlightSelectionBinding? = null
    private val bindingView: FragmentReissueFlightSelectionBinding get() = _bindingView!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this._bindingView = FragmentReissueFlightSelectionBinding.inflate(inflater, container, false)
        this.bindingView.root.setOnClickListener {  }
        return this.bindingView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val someFlights = sharedViewModel.reissueEligibilityResponse?.flights
        Log.d("Before Flights", "$someFlights")
        if (someFlights != null) {
            sharedViewModel.manualFlights = someFlights
            Log.d("Before Flights added", "$someFlights")
        }
        setUpFlightSelectionAdapter(someFlights)

//        if(sharedViewModel.reissueEligibilityResponse?.selectFlightMsg == null) {
//            bindingView.flightMsgCard.visibility = View.GONE
//        } else {
//            bindingView.flightMsgCard.visibility = View.VISIBLE
//            bindingView.flightSelectionMsg.text = sharedViewModel!!.reissueEligibilityResponse!!.selectFlightMsg
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingView = null
    }


    private fun setUpFlightSelectionAdapter(flights: List<ReissueFlight>?) {
        val travellerFlightList = bindingView.travellerFlightList
        flightSelectionAdapter = flights?.let {
            val isFullJourneySelect =
                sharedViewModel.reissueEligibilityResponse!!.isFullJourneySelect
//            val isManual = sharedViewModel.reissueEligibilityResponse!!.automationSupported
//            if (!isManual) {
                sharedViewModel.selectedFlights.value?.clear()
                it.apply {
                    it.forEach {flight ->
                        flight.isFlightSelected = true
                        sharedViewModel.addSelectedFlights(flight)
                    }
                }
//            }

            FlightSelectionAdapter(it, isFullJourneySelect) { position, check ->
                if (check) {
                    sharedViewModel.addSelectedFlights(flights[position])
                    flights[position].isFlightSelected = true
                } else {
                    sharedViewModel.removeFlight(flights[position])
                    flights[position].isFlightSelected = false
                }
            }
        }

        travellerFlightList.adapter = flightSelectionAdapter
        val mLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        travellerFlightList.layoutManager = mLayoutManager
    }
}