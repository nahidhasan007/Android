package com.example.retrofitapi.ui.list

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitapi.R
import com.example.retrofitapi.model.Model
import com.example.retrofitapi.network.RetrofitApi
import com.example.retrofitapi.network.RetrofitHelper


class ListPosts : Fragment(), ClickHandler {
    private val retrofitModelView:RetrofitModelView by viewModels {
        ListModelFactory(ListRepository(RetrofitHelper.getInstance().create(RetrofitApi::class.java)))
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_list_posts, container, false)
        val textData = view.findViewById<TextView>(R.id.testData)
        val recyclerView = view.findViewById<RecyclerView>(R.id.postList)
        val adapter = Adapter(this)
        recyclerView.adapter = adapter
        val mLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager
        retrofitModelView.checkPosts.observe(viewLifecycleOwner) {

           adapter.setData(it)

        }
        return view
    }

    override fun postClicked(post: Model, position: Int) {
       // Log.i(TAG,post.id.toString()+post.title+position)
        val bundle = Bundle()
        bundle.putInt("Id",post.id)
        findNavController().navigate(R.id.action_listPosts_to_singlePost,bundle)
    }


}