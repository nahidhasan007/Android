package net.sharetrip.b2b.view.more.quickPassengers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import net.sharetrip.b2b.R
import net.sharetrip.b2b.databinding.FragmentQuickPassengerBinding
import net.sharetrip.b2b.localdb.LocalDataBase
import net.sharetrip.b2b.util.EventObserver
import net.sharetrip.b2b.view.flight.booking.ui.ListItemClickListener
import net.sharetrip.b2b.view.more.model.QuickPassenger

class QuickPassengerFragment : Fragment(), ListItemClickListener<QuickPassenger> {
    lateinit var bindingView: FragmentQuickPassengerBinding
    lateinit var adapter: QuickPassengerAdapter
    var passengerList: List<QuickPassenger> = ArrayList()

    val viewModel: QuickPassengerVM by lazy {
        val dao = LocalDataBase.getDataBase(requireContext()).quickPassengerDao()
        QuickPassengerVMFactory(QuickPassengerRepo(dao)).create(
            QuickPassengerVM::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingView = FragmentQuickPassengerBinding.inflate(layoutInflater, container, false)
        bindingView.viewModel = viewModel
        adapter = QuickPassengerAdapter(this)
        bindingView.recyclerViewQuickPassenger.adapter = adapter

        bindingView.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.passengerList.observe(viewLifecycleOwner, EventObserver {
            passengerList = it
            adapter.addPassengerList(passengerList)
        })

        bindingView.btnAddPassenger.setOnClickListener {
            findNavController().navigate(R.id.action_add_quick_passenger)
        }

        bindingView.btnAddMorePassenger.setOnClickListener {
            findNavController().navigate(R.id.action_add_quick_passenger)
        }

        viewModel.dataFound.observe(viewLifecycleOwner, EventObserver {
            if (it) {
                bindingView.layoutPassengerList.visibility = View.VISIBLE
                bindingView.btnAddMorePassenger.visibility = View.VISIBLE
                bindingView.layoutNoPassenger.visibility = View.GONE
            } else {
                bindingView.layoutPassengerList.visibility = View.GONE
                bindingView.btnAddMorePassenger.visibility = View.GONE
                bindingView.layoutNoPassenger.visibility = View.VISIBLE
            }
        })

        return bindingView.root
    }

    override fun onClickItem(data: QuickPassenger) {
        findNavController().navigate(R.id.action_add_quick_passenger, bundleOf(ARG_QUICK_PASSENGER to data))
    }

    override fun onResume() {
        super.onResume()
        if (passengerList.isNotEmpty()) {
            adapter.addPassengerList(passengerList)
            bindingView.layoutPassengerList.visibility = View.VISIBLE
            bindingView.btnAddMorePassenger.visibility = View.VISIBLE
            bindingView.layoutNoPassenger.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_QUICK_PASSENGER = "ARG_QUICK_PASSENGER"
    }
}