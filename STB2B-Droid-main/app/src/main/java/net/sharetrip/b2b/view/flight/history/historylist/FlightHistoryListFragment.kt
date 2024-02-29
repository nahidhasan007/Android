package net.sharetrip.b2b.view.flight.history.historylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.FragmentFlightHistoryListBinding
import net.sharetrip.b2b.network.FlightEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.view.flight.history.model.FlightHistory

class FlightHistoryListFragment : Fragment {
    var pageNo = 0
    val adapter = FlightHistoryAdapter()
    private var isLoading = true
    private var count = 0

    private val viewModel by lazy {
        val endPoint = ServiceGenerator.createService(FlightEndPoint::class.java)
        FlightHistoryVMFactory(FlightHistoryRepo(endPoint), pageNo).create(
            FlightHistoryVM::class.java
        )
    }

    constructor() : super()
    constructor(pageNo: Int) {
        this.pageNo = pageNo
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentFlightHistoryListBinding.inflate(layoutInflater, container, false)
        bindingView.viewModel = viewModel

        bindingView.recyclerFlightHistory.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        bindingView.recyclerFlightHistory.layoutManager = layoutManager

        bindingView.recyclerFlightHistory.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleThreshold = 1
                val totalItemCount: Int = adapter.itemCount
                val lastVisibleItem: Int = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold && totalItemCount < count) {
                    viewModel.getFlightHistory(totalItemCount)
                    isLoading = true
                }
            }
        })

        viewModel.flightHistoryList.observe(viewLifecycleOwner) {
            count = it.count
            adapter.addItem(it.data as ArrayList<FlightHistory>)
            isLoading = false
        }

        viewModel.showMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })

        return bindingView.root
    }

    companion object {
        const val FLIGHT_HISTORY_DETAILS = "flight_history_details"
    }
}
