package com.example.messutilities.demopaging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.messutilities.databinding.FragmentMemberDetailsBinding


class DemoPagingAdapter : PagingDataAdapter<Any, DemoPagingAdapter.ViewHolder>(DemoDifferCallback) {

    override fun onBindViewHolder(holder: DemoPagingAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DemoPagingAdapter.ViewHolder {
        val binding =
            FragmentMemberDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(demoBinding: FragmentMemberDetailsBinding) :
        RecyclerView.ViewHolder(demoBinding.root) {

    }

    companion object {
        val DemoDifferCallback: DiffUtil.ItemCallback<Any> =
            object : DiffUtil.ItemCallback<Any>() {
                override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                    TODO("Not yet implemented")
                }

                override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                    TODO("Not yet implemented")
                }
            }
    }
}