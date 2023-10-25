package com.example.messutilities.mealschart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.messutilities.databinding.FragmentMemberBinding
import com.example.messutilities.network.BuildRetrofit
import com.example.messutilities.network.FakeApi

class MealsChartFragment: Fragment() {
    private lateinit var binding : FragmentMemberBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiService = BuildRetrofit.createService(FakeApi::class.java)

    }
}