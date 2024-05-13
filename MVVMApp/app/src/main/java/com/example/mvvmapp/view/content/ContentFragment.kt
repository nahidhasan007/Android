package com.example.mvvmapp.view.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.mvvmapp.R
import com.example.mvvmapp.databinding.ContentFragmentBinding
import com.example.mvvmapp.view.content.adapter.ContentAdapter

class ContentFragment : Fragment() {

    lateinit var binding: ContentFragmentBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[ContentViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.content_fragment, container, false
        )
        val player = ExoPlayer.Builder(requireContext()).build()


        val mediaItem = MediaItem.fromUri("your_video_uri_here")
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contentAdapter = ContentAdapter()
        binding.contentItemsRecycler.adapter = contentAdapter

        viewModel.contents.observe(viewLifecycleOwner) {
            if (it != null) {
                contentAdapter.submitList(it)
            }
        }


    }


}