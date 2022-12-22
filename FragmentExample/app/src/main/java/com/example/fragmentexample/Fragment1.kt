package com.example.fragmentexample

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


class Fragment1 : Fragment(R.layout.fragment1) {
    lateinit var inputText: EditText
    lateinit var pref : SharedPreferences

    @SuppressLint("CommitPrefEdits")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = layoutInflater.inflate(R.layout.fragment1,container,false)
        val btn = rootView.findViewById<Button>(R.id.fragment1btn)
        //val btn = findViewById<Button>(R.id.fragment1btn)
        //val btn2 = findViewById<Button>(R.id.fragment2btn)
        pref = activity?.getSharedPreferences("Login", MODE_PRIVATE)!!
        //btn.setOnClickListener() { replaceFragment(Fragment1()) }
        inputText = rootView.findViewById<EditText>(R.id.inputText)

        if(isLogin()){
            val appname = pref?.getString("appName","Default")
            if (appname != null) {
                passDataCom(appname)
            }
        }

        btn.setOnClickListener() {
            passDataCom(inputText.text.toString())
            inputText.setText("")

        }
        return rootView
    }

    fun passDataCom(editText: String){
        val bundle = Bundle()
        bundle.putString("Input_text",editText)
        val fm = requireActivity().supportFragmentManager
        val ft = fm.beginTransaction()
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val frag2 = Fragment2()
        frag2.arguments = bundle
        ft.replace(R.id.fragmentContainer,frag2)
        //transaction.disallowAddToBackStack()
       if(saveInput()) {
           ft.commit()
       }
        //transaction.commitNowAllowingStateLoss()
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,fragment)
        fragmentTransaction.commit()
    }

    @SuppressLint("CommitPrefEdits")
    private fun saveInput():Boolean{
        val editor = pref?.edit()
        editor?.putBoolean("Login", true)
        editor?.putString("appName", inputText.text.toString())
        editor?.apply()
        return true

    }
    private fun isLogin():Boolean{
        val check = pref?.getBoolean("Login",false)
        Log.i(TAG, check.toString())
        return check == true
    }
}