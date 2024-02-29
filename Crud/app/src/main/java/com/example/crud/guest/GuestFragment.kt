package com.example.crud.guest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.crud.R
import com.example.crud.databinding.FragmentGuestBinding
import com.example.crud.guest.model.Guest

class GuestFragment : Fragment() {
    lateinit var guestViewModel: GuestViewModel
    lateinit var binding: FragmentGuestBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_guest, container, false)
        guestViewModel = ViewModelProvider(this)[GuestViewModel::class.java]
        val name = binding.name.text.toString()
        val email = binding.email.text.toString()
        val phone = binding.phone.text.toString()
        val password = binding.password.text.toString()
        val guest = Guest(1,name, email, phone, password)
        binding.button.setOnClickListener() {
            guestViewModel.addGuest(guest)
        }
        return binding.root
    }

    companion object {

    }
}