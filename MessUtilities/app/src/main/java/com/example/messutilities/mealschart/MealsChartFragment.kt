package com.example.messutilities.mealschart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.messutilities.databinding.FragmentMemberBinding

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
}