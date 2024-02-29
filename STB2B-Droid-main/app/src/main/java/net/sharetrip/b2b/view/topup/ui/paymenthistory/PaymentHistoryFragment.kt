package net.sharetrip.b2b.view.topup.ui.paymenthistory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.databinding.FragmentPaymentHistoryBinding
import net.sharetrip.b2b.network.PaymentEndPoint
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.view.topup.model.PaymentHistory

class PaymentHistoryFragment : Fragment() {
    private val adapter = PaymentHistoryAdapter()
    private var isLoading = true
    private var count = 0

    private val viewModel by lazy {
        val endPoint = ServiceGenerator.createService(PaymentEndPoint::class.java)
        ViewModelProvider(this, PaymentHistoryVMFactory(PaymentHistoryRepo(endPoint))).get(
            PaymentHistoryVM::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bindingView = FragmentPaymentHistoryBinding.inflate(layoutInflater, container, false)

        bindingView.viewModel = viewModel

        bindingView.recyclerPaymentHistory.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())
        bindingView.recyclerPaymentHistory.layoutManager = layoutManager

        bindingView.recyclerPaymentHistory.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleThreshold = 1
                val totalItemCount: Int = layoutManager.itemCount
                val lastVisibleItem: Int = layoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold && totalItemCount < count) {
                    viewModel.loadPaymentHistory(totalItemCount)
                    isLoading = true
                }
            }
        })

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.paymentHistoryList.observe(viewLifecycleOwner) {
            count = it.count
            adapter.addItem(it.data as ArrayList<PaymentHistory>)
            isLoading = false
        }

        viewModel.showMessage.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        return bindingView.root
    }
}
