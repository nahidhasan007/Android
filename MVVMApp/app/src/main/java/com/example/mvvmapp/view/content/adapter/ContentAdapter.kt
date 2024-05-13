package com.example.mvvmapp.view.content.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmapp.model.Content
import com.example.mvvmapp.view.content.viewholder.ContentViewHolder

class ContentAdapter() : RecyclerView.Adapter<ContentViewHolder>() {

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(list: List<Content>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        return ContentViewHolder.create(parent)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<Content>() {
            override fun areItemsTheSame(
                oldItem: Content,
                newItem: Content
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Content,
                newItem: Content
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}