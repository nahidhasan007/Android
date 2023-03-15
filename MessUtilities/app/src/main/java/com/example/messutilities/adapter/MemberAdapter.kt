package com.example.messutilities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.messutilities.databinding.SingleMemberItemBinding
import com.example.messutilities.model.Members

class MemberAdapter(private var members : ArrayList<Members>) :
    RecyclerView.Adapter<MemberAdapter.MemberAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberAdapterViewHolder {
        val viewHolder = SingleMemberItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MemberAdapterViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return members.size
    }

    override fun onBindViewHolder(holder: MemberAdapterViewHolder, position: Int) {
        holder.binding.memberName.text = members[position].name
        holder.binding.phone.text = members[position].phone
    }

    inner class MemberAdapterViewHolder(val binding: SingleMemberItemBinding) :
       RecyclerView.ViewHolder(binding.root)
}