package net.sharetrip.b2b.view.transaction.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.databinding.FragmentTransactionDetailsBinding
import net.sharetrip.b2b.network.ServiceGenerator
import net.sharetrip.b2b.network.TransactionEndPoint
import net.sharetrip.b2b.view.transaction.view.transactionlist.TransactionListFragment
import net.sharetrip.b2b.view.transaction.viewmodel.TransactionDetailsVM
import net.sharetrip.b2b.view.transaction.viewmodel.TransactionDetailsVMFactory

class TransactionDetailsFragment : Fragment() {
    private lateinit var bindingView: FragmentTransactionDetailsBinding
    lateinit var uuid: String
    private val viewModel by lazy {
        val endPoint = ServiceGenerator.createService(TransactionEndPoint::class.java)
        uuid = arguments?.getString(TransactionListFragment.ARGUMENT_UUID_CODE)!!
        ViewModelProvider(this, TransactionDetailsVMFactory(TransactionDetailsRepo(endPoint))).get(
            TransactionDetailsVM::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView = FragmentTransactionDetailsBinding.inflate(inflater, container, false)
        bindingView.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        bindingView.viewModel = viewModel
        viewModel.loadTransactionDetails(uuid)

        viewModel.showMessage.observe(viewLifecycleOwner,
            { if (it != null) Toast.makeText(context, it, Toast.LENGTH_LONG).show() })

        return bindingView.root
    }
}