package com.example.messutilities.members

import com.example.messutilities.R
import com.example.messutilities.adapter.MemberAdapter
import com.example.messutilities.databinding.FragmentMemberBinding
import com.example.messutilities.model.Members
import com.example.messutilities.shared.BaseViewModel
import com.example.shared.widgets.BaseFragment

class MemberFragment : BaseFragment<FragmentMemberBinding>() {

    private var memberAdapter : MemberAdapter?= null
    override fun layoutId(): Int = R.layout.fragment_member

    override fun getViewModel(): BaseViewModel? = null

    override fun initOnCreateView() {
        var members = arrayListOf<Members>()
        for (i in 0..5){
            val member = Members("Nahid", "+8801728242739")
            members.add(member)
        }
        memberAdapter = MemberAdapter(members)
        bindingView.memberList.adapter = memberAdapter
    }


}