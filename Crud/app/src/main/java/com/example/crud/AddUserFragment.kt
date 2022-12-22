package com.example.crud

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crud.data.User
import com.example.crud.data.UserViewModel
import com.example.crud.databinding.FragmentAddUserBinding

class AddUserFragment : Fragment() {
    lateinit var userViewModel: UserViewModel
    lateinit var binding:FragmentAddUserBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_add_user, container, false)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        /*val name = view.findViewById<EditText>(R.id.Name)
        val phone = view.findViewById<EditText>(R.id.Phone)
        val address = view.findViewById<EditText>(R.id.Address)

         */
        // Get views using binding
        val user_data = arguments?.getParcelable<User>("UserInfo")
       /* val addbtn = view.findViewById<Button>(R.id.addbutton)
        val deletebtn = view.findViewById<Button>(R.id.deleteButton)

        */
        binding.deleteButton.visibility = View.GONE
        if (user_data!=null) {
                binding.Name.setText(user_data.name)
                binding.Phone.setText(user_data.phone)
                binding.Address.setText(user_data.address)
                binding.addbutton.setText("Update")
                binding.deleteButton.visibility = View.VISIBLE
                binding.deleteButton.setOnClickListener(){
                    val uid = user_data.id
                    val uName = binding.Name.text.toString()
                    val uPhone = binding.Phone.toString()
                    val uAddress = binding.Address.text.toString()
                    val user = User(uid, uName, uPhone, uAddress)
                    userViewModel.userDelete(user)
                    findNavController().navigate(R.id.action_addUserFragment2_to_userListFragment)
                }
                binding.addbutton.setOnClickListener() {
                    val uid = user_data.id
                    val uName = binding.Name.text.toString()
                    val uPhone = binding.Phone.text.toString()
                    val uAddress = binding.Address.text.toString()
                    val newUser = User(uid, uName, uPhone, uAddress)
                    userViewModel.userUpdate(newUser)
                    findNavController().navigate(R.id.action_addUserFragment2_to_userListFragment)
                }
            }
        else {
            binding.addbutton.setOnClickListener() {
                val Name = binding.Name.text.toString()
                val Phone = binding.Phone.text.toString()
                val Address = binding.Address.text.toString()
                val user = User(0, Name, Phone, Address)
                // val recyclerView = view.findViewById<RecyclerView>(R.id.UserInfo)
                userViewModel.addUser(user)
                //val adapter = Adapter()
                //val mLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                //recyclerView.layoutManager = mLayoutManager
                //adapter.setData(listOf(user))
                findNavController().navigate(R.id.action_addUserFragment2_to_userListFragment)
                //val db = userViewModel.readAllUserData()
                //Log.i(TAG, db.toString())


            }
        }

        return binding.root
    }
}

