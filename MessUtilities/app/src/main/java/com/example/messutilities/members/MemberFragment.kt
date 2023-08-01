package com.example.messutilities.members

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.messutilities.R
import com.example.messutilities.adapter.MemberAdapter
import com.example.messutilities.databinding.FragmentMemberBinding
import com.example.messutilities.model.Members
import com.example.messutilities.shared.BaseApplication
import com.example.messutilities.shared.BaseViewModel
import com.example.shared.widgets.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MemberFragment : BaseFragment<FragmentMemberBinding>(), MemberAdapter.MemberDetails{

    @Inject
    lateinit var memberRepository: MemberRepository
    private var memberAdapter : MemberAdapter?= null
    override fun layoutId(): Int = R.layout.fragment_member

    override fun getViewModel(): BaseViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memberRepository.saveMember("nahidhasan007@gmail.com","17008")
    }

    override fun initOnCreateView() {
        var members = arrayListOf<Members>()
        for (i in 0..5){
            val member = Members(1,"Nahid", "+8801728242739")
            members.add(member)
        }
        memberAdapter = MemberAdapter(members,this)
        bindingView.memberList.adapter = memberAdapter

    }

    override fun seeMemberDetails(members: Members) {
       findNavController().navigate(
           R.id.action_memberFragment_to_memberDetails,
           bundleOf(ARG_MEMBERS to members)
       )
    }

    companion object{
        const val ARG_MEMBERS = "MEMBERS"
    }


}