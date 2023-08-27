package com.example.messutilities.demopaging

import androidx.lifecycle.lifecycleScope
import com.example.messutilities.R
import com.example.messutilities.databinding.FragmentMemberDetailsBinding
import com.example.messutilities.demopaging.adapter.DemoPagingAdapter
import com.example.messutilities.networkmanager.NetworkManager
import com.example.messutilities.shared.BaseViewModel
import com.example.shared.widgets.BaseFragment

class DemoPagingFragment : BaseFragment<FragmentMemberDetailsBinding>(){
    override fun layoutId(): Int = R.layout.fragment_member_details
    private val adapter = DemoPagingAdapter()
    private val viewModel : DemoPagingViewModel by lazy {
        DemoPagingVMF(
            DemoPagingRepository(NetworkManager.postApiService),
            NetworkManager.postApiService
        ).create(DemoPagingViewModel::class.java)
    }

    override fun getViewModel(): BaseViewModel? = viewModel

    override fun initOnCreateView() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.demoLiveData.collect(){
              adapter.submitData(it)
            }
        }
    }
}