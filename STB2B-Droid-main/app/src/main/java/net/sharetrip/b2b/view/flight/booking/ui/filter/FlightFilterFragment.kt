package net.sharetrip.b2b.view.flight.booking.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentFlightFilterBinding
import net.sharetrip.b2b.view.flight.booking.model.FilterParams
import net.sharetrip.b2b.view.flight.booking.model.FlightFilter
import net.sharetrip.b2b.view.flight.booking.model.FlightSearch

class FlightFilterFragment : Fragment() {
    private val flightListDataArgs by navArgs<FlightFilterFragmentArgs>()
    private lateinit var flightFilter: FlightFilter
    private lateinit var flightSearch: FlightSearch
    private val flightFilterVM: FlightFilterVM by lazy {
        FlightFilterVMFactory().create(FlightFilterVM::class.java)
    }

    lateinit var bindingView: FragmentFlightFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        flightFilter = flightListDataArgs.flightFilter
        flightSearch = flightListDataArgs.flightSearch

        bindingView = FragmentFlightFilterBinding.inflate(inflater, container, false)
            .apply {
                viewModel = flightFilterVM
                lifecycleOwner = viewLifecycleOwner
                textViewFilterTitle.text = flightListDataArgs.totalFlight

                toolbar.setNavigationOnClickListener { view ->
                    view.findNavController().navigateUp()
                }

                buttonApplySearchFilter.setOnClickListener { view ->
                    setDataForResult(view)
                }

                flightFilterVM.liveData.observe(viewLifecycleOwner) { enum ->
                    val bundle = Bundle()
                    bundle.putInt(ARG_FILTER_TITLE, enum.filterCode)
                    bundle.putParcelable(ARG_FLIGHT_FILTER, flightFilter)
                    findNavController().navigate(R.id.action_flight_filter_to_bottom_filter_list, bundle)
                }

                textViewFilter.setOnClickListener {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.filter_reset),
                        Toast.LENGTH_SHORT
                    ).show()
                    flightFilterVM.clearFilterData()
                }

               flightSearchDetails = flightSearch
            }

        return bindingView.root
    }

    private fun setDataForResult(view: View) {
        val params = FilterParams()
        params.isRefundable = FilterConstrains.isRefundableCodeSets
        params.stops = FilterConstrains.stopCodeSets
        params.weight = FilterConstrains.weightCodeSets
        params.outbound = FilterConstrains.outboundCodeSets?.elementAtOrNull(0)
        params.returnTime = FilterConstrains.returnCodeSets?.elementAtOrNull(0)

        val bundle = Bundle()
        bundle.putParcelable(FLIGHT_FILTER_PARAMS_ARGS, params)
        view.findNavController().previousBackStackEntry?.savedStateHandle?.set(
            RESULT_FLIGHT_FILTER,
            bundle
        )
        view.findNavController().popBackStack()
    }

    companion object {
        const val ARG_FILTER_TITLE = "arg_filter_title"
        const val ARG_FLIGHT_FILTER = "arg_flight_filter"
        const val RESULT_FLIGHT_FILTER = "ResultFlightFilter"
        const val FLIGHT_FILTER_PARAMS_ARGS = "FlightFilterParamsArgs"
    }
}
