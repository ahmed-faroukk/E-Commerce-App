package com.example.ECommerceApp.presentation.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.ECommerceApp.R
import com.example.ECommerceApp.databinding.FragmentOnboardingOneBinding

class onboardingOne : Fragment() {

    lateinit var binding : FragmentOnboardingOneBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingOneBinding.inflate(inflater ,container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewpager2)
        binding.boardingOneBtn.setOnClickListener(View.OnClickListener {

            viewPager?.currentItem = 1
        })


        return binding.root
    }

}