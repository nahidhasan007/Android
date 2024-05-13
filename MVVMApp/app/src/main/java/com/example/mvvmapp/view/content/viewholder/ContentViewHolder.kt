package com.example.mvvmapp.view.content.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmapp.databinding.ContentItemBinding
import com.example.mvvmapp.model.Content

class ContentViewHolder(
    private val binding: ContentItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    private var player: ExoPlayer? = null
    fun bind(content: Content) {


        binding.descriptionTextView.text = content.description
        binding.titleTextView.text = content.title

        player = ExoPlayer.Builder(itemView.context).build()
        binding.contentVideoView.player = player
        val mediaItem = MediaItem.fromUri(content.url)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
    }


    companion object {
        fun create(parent: ViewGroup) =
            ContentViewHolder(
                ContentItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }
}