package com.example.messutilities.members.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.messutilities.databinding.SingleMemberItemBinding
import com.example.messutilities.members.model.Members

class MemberAdapter(private val members : ArrayList<Members>) :
    RecyclerView.Adapter<MemberAdapter.MemberAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberAdapterViewHolder {
        val viewHolder = SingleMemberItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MemberAdapterViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return members.size
    }

    override fun onBindViewHolder(holder: MemberAdapterViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    inner class MemberAdapterViewHolder(binding: SingleMemberItemBinding) :
       RecyclerView.ViewHolder(binding.root)
}