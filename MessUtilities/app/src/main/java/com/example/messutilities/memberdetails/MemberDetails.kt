package com.example.messutilities.memberdetails

import android.os.Bundle
import com.example.messutilities.R
import com.example.messutilities.databinding.FragmentMemberDetailsBinding
import com.example.messutilities.members.MemberFragment.Companion.ARG_MEMBERS
import com.example.messutilities.model.Members
import com.example.messutilities.shared.BaseViewModel
import com.example.shared.widgets.BaseFragment


class MemberDetails : BaseFragment<FragmentMemberDetailsBinding>() {
    override fun layoutId(): Int  = R.layout.fragment_member_details

    override fun getViewModel(): BaseViewModel? = null
    private lateinit var member : Members

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        member = requireArguments().getParcelable(ARG_MEMBERS)!!
    }

    override fun initOnCreateView() {
        bindingView.apply {
            memberNameText.text = member.name
            phone.text = member.phone
        }
    }


}