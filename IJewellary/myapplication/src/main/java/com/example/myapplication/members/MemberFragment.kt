package com.example.messutilities.members

import com.example.messutilities.R
import com.example.messutilities.databinding.FragmentMemberBinding
import com.example.messutilities.members.adapter.MemberAdapter
import com.example.messutilities.members.model.Members
import com.example.shared.widgets.BaseFragment
import com.example.shared.widgets.BaseViewModel

class MemberFragment : BaseFragment<FragmentMemberBinding>() {
    private lateinit var members : ArrayList<Members>

    override fun layoutId(): Int = R.layout.fragment_member

    override fun getViewModel(): BaseViewModel? = null

    override fun initOnCreateView() {
        for (i in 0..5){
            val member = Members("Nahid","+8801728242739")
            members.add(member)
        }
        val adapter = MemberAdapter(members)
        bindingView.memberList.adapter = adapter
    }


}