package com.example.retrofitapi.ui.singlePost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.retrofitapi.R
import com.example.retrofitapi.databinding.FragmentSinglePostBinding
import com.example.retrofitapi.network.RetrofitApi
import com.example.retrofitapi.network.RetrofitHelper

class SinglePost : Fragment() {
    private val singlePostModel: SinglePostModelView by viewModels {
        SinglePostFactory(
            arguments?.get("Id") as Int,
            SinglePostRepo(RetrofitHelper.getInstance().create(RetrofitApi::class.java))
        )

    }
    lateinit var binding:FragmentSinglePostBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_single_post, container, false)

        binding.post = singlePostModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.commentBtn.setOnClickListener()
        {
            val bundle = Bundle()
            bundle.putInt("Id",arguments?.get("Id") as Int)
            findNavController().navigate(R.id.action_singlePost_to_detailPost,bundle)
        }

        return binding.root
    }


}