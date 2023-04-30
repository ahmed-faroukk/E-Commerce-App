package com.example.ECommerceApp.presentation.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.ECommerceApp.R
import com.example.ECommerceApp.databinding.FragmentOnboardingTwoBinding

class onboardingTwo : Fragment() {

    lateinit var binding: FragmentOnboardingTwoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingTwoBinding.inflate(inflater, container, false)

        val viewpager = activity?.findViewById<ViewPager2>(R.id.viewpager2)
        binding.boardingTwoBtn.setOnClickListener {
            viewpager?.currentItem = 2
        }


        return binding.root
    }


}