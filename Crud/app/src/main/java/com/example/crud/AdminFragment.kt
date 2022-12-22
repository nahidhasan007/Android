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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.crud.admin.AdminViewModel
import com.example.crud.admin.Superadmin
import com.example.crud.databinding.FragmentAddUserBinding
import com.example.crud.databinding.FragmentAdminBinding

class AdminFragment : Fragment() {
    private lateinit var adminViewModel: AdminViewModel
    private lateinit var adminBinding: FragmentAdminBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        adminBinding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_admin, container, false)
        adminViewModel = ViewModelProvider(this)[AdminViewModel::class.java]
        //var check = 0
        //var adminName: String
        //var adminPassword: String
        adminViewModel.readAdmin.observe(viewLifecycleOwner){
            //adminName = it[0].username
            //adminPassword = it[0].password

        }
        /*val admin = Superadmin(0,"Admin","Admin")
        if(check<=2){
            adminViewModel.addAdmin(admin)
        }
        ?

        val loginBtn = view.findViewById<Button>(R.id.LoginBtn)
        val username = view.findViewById<EditText>(R.id.userName)
        val password = view.findViewById<EditText>(R.id.password)

         */
        adminBinding.LoginBtn.setOnClickListener()
        {
            val uname = adminBinding.userName.text.toString()
            val pass = adminBinding.password.text.toString()
            adminViewModel.adminLogin(uname,pass)
            adminViewModel.isLoginSuccessful.observe(viewLifecycleOwner)
            {
                if(it)
                {
                    findNavController().navigate(R.id.action_adminFragment_to_userListFragment)
                }
                else{
                    Toast.makeText(adminBinding.root.context, "Invalid UserName or PassWord!", Toast.LENGTH_SHORT).show()
                }
            }

        }


        return adminBinding.root
    }

}