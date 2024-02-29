package net.sharetrip.b2b.view.transaction.view.transactionlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentTransactionListBinding
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.network.TransactionEndPoint
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.util.showToast
import net.sharetrip.b2b.view.transaction.model.TransactionFilter
import net.sharetrip.b2b.view.transaction.view.filter.TransactionListFilterFragment
import net.sharetrip.b2b.view.transaction.view.filter.TransactionListFilterFragment.Companion.RESULT_TRANSACTION_FILTER
import net.sharetrip.b2b.view.transaction.viewmodel.TransactionListVM
import net.sharetrip.b2b.view.transaction.viewmodel.TransactionListVMFactory
import java.net.URLEncoder

class TransactionListFragment : Fragment() {
    private val transactionListAdapter = TransactionListAdapter()
    private var isLoading = true
    private var isFilter = false
    private var count = 0
    private var filterData: String? = null

    private val viewModel by lazy {
        val endPoint = ServiceGenerator.createService(TransactionEndPoint::class.java)
        TransactionListVMFactory(TransactionListRepo(endPoint)).create(
            TransactionListVM::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentTransactionListBinding.inflate(inflater, container, false)
        bindingView.viewModel = viewModel
        bindingView.recyclerTransactionList.adapter = transactionListAdapter
        val layoutManager = LinearLayoutManager(requireContext())
        bindingView.recyclerTransactionList.layoutManager = layoutManager

        viewModel.transactionList.observe(viewLifecycleOwner, EventObserver {
            it.count?.let { it1-> count = it1 }
            it.data?.let { it2 -> transactionListAdapter.addItems(it2, isFilter) }
            isLoading = false
            isFilter = false
        })

        bindingView.recyclerTransactionList.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleThreshold = 1
                val totalItemCount: Int = transactionListAdapter.itemCount
                val lastVisibleItem: Int = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && (totalItemCount <= lastVisibleItem + visibleThreshold) && totalItemCount < count) {
                    if (totalItemCount <= viewModel.dataSize) {
                        viewModel.getTransactionList(totalItemCount, filterData)
                        isLoading = true
                    }
                }
            }
        })

        viewModel.showMessage.observe(viewLifecycleOwner, EventObserver {
            showToast(requireContext(), it)
        })

        bindingView.buttonFilter.setOnClickListener {
            var filter = ""
            if (filterData != null) {
                filter = filterData as String
            }

            findNavController().navigate(
                R.id.action_transaction_list_dest_to_transaction_list_filter_dest,
                bundleOf(TransactionListFilterFragment.ARG_TRANSACTION_FILTER_OBJECT to filter)
            )
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>(
            RESULT_TRANSACTION_FILTER
        )?.observe(viewLifecycleOwner) { result ->
            val transactionFilter: TransactionFilter? =
                result.getParcelable(TransactionListFilterFragment.ARG_TRANSACTION_FILTER_OBJECT)

            filterData = Gson().toJson(transactionFilter)
            viewModel.getTransactionList(0, URLEncoder.encode(filterData, "utf-8"))
            isFilter = true
        }

        return bindingView.root
    }

    companion object {
        const val ARGUMENT_UUID_CODE = "ARGUMENT_UUID_CODE"
    }
}