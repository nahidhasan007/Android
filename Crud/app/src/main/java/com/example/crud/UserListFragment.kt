package com.example.crud

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crud.data.User
import com.example.crud.data.UserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class UserListFragment : Fragment(),ClickPostHandler {
    private lateinit var userViewModel : UserViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
       val  view =  inflater.inflate(R.layout.fragment_user_list, container, false)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        val floatingActionButton = view.findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        floatingActionButton.setOnClickListener(){
            findNavController().navigate(R.id.action_userListFragment_to_addUserFragment2)

        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.UserInfo)
        val adapter = Adapter( this)
        recyclerView.adapter = adapter
        val mLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager
        userViewModel.readData.observe(viewLifecycleOwner){
            adapter.setData(it)
        }
        return view
    }
    override fun userClicked(post: User, position: Int) {
       // Log.i(TAG,post.name+" "+position)
        val bundle = bundleOf("UserInfo" to post)
        findNavController().navigate(R.id.action_userListFragment_to_addUserFragment2,bundle )


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menuitems,menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean  = when(item.itemId) {
        R.id.logout -> {
            findNavController().navigate(R.id.action_userListFragment_to_adminFragment2)

            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }


    }


}