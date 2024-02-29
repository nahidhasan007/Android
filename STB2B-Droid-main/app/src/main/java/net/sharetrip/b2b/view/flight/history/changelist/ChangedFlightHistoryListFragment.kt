package net.sharetrip.b2b.view.flight.history.changelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentFlightHistoryChangeListBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.util.showToast
import net.sharetrip.b2b.view.flight.history.changelistfilter.ChangeTicketListFilterFragment
import net.sharetrip.b2b.view.flight.history.changelistfilter.ChangeTicketListFilterFragment.Companion.RESULT_CHANGED_FLIGHT_FILTER
import net.sharetrip.b2b.view.flight.history.model.ChangedFlightFilter
import java.net.URLEncoder

class ChangedFlightHistoryListFragment : Fragment() {
    val changeTicketListAdapter = ChangedFlightHistoryListAdapter()
    private var isLoading = true
    private var isFilter = false
    private var count = 0
    private var filterData: String? = null

    private val viewModel by lazy {
        val endPoint = ServiceGenerator.createService(FlightEndPoint::class.java)
        ChangedFlightHistoryListVMFactory(ChangedFlightHistoryListRepo(endPoint)).create(
            ChangedFlightHistoryListVM::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView =
            FragmentFlightHistoryChangeListBinding.inflate(layoutInflater, container, false)
        bindingView.viewModel = viewModel
        bindingView.recyclerFlightChangeTicketList.adapter = changeTicketListAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        bindingView.recyclerFlightChangeTicketList.layoutManager = layoutManager

        viewModel.changedFlightHistoryDetailsList.observe(viewLifecycleOwner, EventObserver {
            count = it.count!!
            it.data?.let { it1 -> changeTicketListAdapter.addItems(it1, isFilter) }
            isLoading = false
            isFilter = false
        })

        bindingView.recyclerFlightChangeTicketList.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleThreshold = 1
                val totalItemCount: Int = changeTicketListAdapter.itemCount
                val lastVisibleItem: Int = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold && totalItemCount < count) {
                    viewModel.getChangeTicketList(totalItemCount, filterData)
                    isLoading = true
                }
            }
        })

        viewModel.showMessage.observe(viewLifecycleOwner, EventObserver {
            showToast(requireContext(), it)
        })

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        bindingView.buttonFilter.setOnClickListener {
            var filter = ""
            if (filterData != null) {
                filter = filterData!!
            }

            findNavController().navigate(
                R.id.action_to_changed_list_filter,
                bundleOf(ChangeTicketListFilterFragment.ARG_CHANGED_FLIGHT_FILTER_OBJECT to filter)
            )
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(
            RESULT_CHANGED_FLIGHT_FILTER
        )?.observe(viewLifecycleOwner) { result ->
            val flightFilter: ChangedFlightFilter? =
                result.getParcelable(ChangeTicketListFilterFragment.ARG_CHANGED_FLIGHT_FILTER_OBJECT)
            if (flightFilter?.bookingDate?.start?.isEmpty() == true){
                flightFilter.bookingDate = null
            }
            filterData = Gson().toJson(flightFilter)
            viewModel.getChangeTicketList(0, URLEncoder.encode(filterData, "utf-8"))
            isFilter = true
        }

        return bindingView.root
    }

    companion object {
        const val ARGUMENT_UUID_CODE = "ARGUMENT_UUID_CODE"
    }
}