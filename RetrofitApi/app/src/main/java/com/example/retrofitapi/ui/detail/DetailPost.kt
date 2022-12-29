package com.example.retrofitapi.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapi.R
import com.example.retrofitapi.network.RetrofitApi
import com.example.retrofitapi.network.RetrofitHelper


class DetailPost : Fragment()  {

   private val detailViewModel: DetailViewModel by viewModels {
            ViewModelFactory(
                arguments?.get("Id") as Int,
                ApiRepository(RetrofitHelper.getInstance().create(RetrofitApi::class.java))
            )

    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_detail_post, container, false)
        //val textData = view.findViewById<TextView>(R.id.postId)
        //textData.setText(" ")
       // textData.text = id.toString()
        val recyclerView = view.findViewById<RecyclerView>(R.id.DetailPost)
        val adapter = DetailAdapter()
        recyclerView.adapter = adapter
        val mLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager
        detailViewModel.detailPost.observe(viewLifecycleOwner)
        {
            adapter.setData(it)
        }

        return view
    }


}