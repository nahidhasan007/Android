package net.sharetrip.b2b.view.flight.booking.ui.segment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.databinding.FragmentSegmentBinding
import net.sharetrip.b2b.util.SHOW_TOOLBAR
import net.sharetrip.b2b.view.flight.booking.model.Flights
import net.sharetrip.b2b.view.flight.booking.ui.flightdetails.FlightDetailsFragment.Companion.ARG_ITEM_FLIGHTS
import net.sharetrip.b2b.view.flight.booking.ui.flightdetails.FlightDetailsFragment.Companion.ARG_ITEM_FLIGHTS_SEGMENT_POSITION
import net.sharetrip.b2b.view.flight.history.historydetails.FlightHistoryDetailsFragment.Companion.ARG_FLIGHT_HISTORY
import net.sharetrip.b2b.view.flight.history.model.FlightHistory

class FlightSegmentFragment : Fragment() {
    private var scrollToPosition: Int = 0
    private val viewModel: FlightSegmentVM by lazy {
        val flights: Flights? = arguments?.getParcelable(ARG_ITEM_FLIGHTS)
        val flightHistory: FlightHistory? = arguments?.getParcelable(ARG_FLIGHT_HISTORY)
        FlightSegmentVmFactory(flights, flightHistory).create(FlightSegmentVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentSegmentBinding.inflate(inflater, container, false)
        scrollToPosition = requireArguments().getInt(ARG_ITEM_FLIGHTS_SEGMENT_POSITION)
        val showToolbar = requireArguments().getBoolean(SHOW_TOOLBAR)
        bindingView.showToolbar = showToolbar

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.segmentList.observe(viewLifecycleOwner, {
            bindingView.flightSegmentRecyclerView.adapter =
                FlightSegmentAdapter(it, viewModel, showToolbar)
            bindingView.flightSegmentRecyclerView.layoutManager?.scrollToPosition(scrollToPosition)
        })

        viewModel.moveToBack.observe(viewLifecycleOwner, {
            findNavController().navigateUp()
        })

        return bindingView.root
    }
}
