package com.example.foodapp.ui.fragments.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.foodapp.R
import com.example.foodapp.adpters.ViewPagerAdapter
import com.example.foodapp.databinding.FragmentViewPagerBinding
import com.example.foodapp.ui.fragments.onboarding.screens.onboardingOne
import com.example.foodapp.ui.fragments.onboarding.screens.onboardingThree
import com.example.foodapp.ui.fragments.onboarding.screens.onboardingTwo


class ViewPagerFragment : Fragment() {
    private lateinit var binding: FragmentViewPagerBinding
    private lateinit var viewPager: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment


        binding = FragmentViewPagerBinding.inflate(inflater ,container, false)
        viewPager = binding.viewpager2
        if (onBoardingFinished())
            findNavController().navigate(R.id.loginFragment)

        val fragmentList = arrayListOf<Fragment>(
            onboardingOne() ,
            onboardingTwo() ,
            onboardingThree()
        )
        val adapter = ViewPagerAdapter(
            fragmentList ,
            requireActivity().supportFragmentManager ,
            lifecycle ,
        viewPager = viewPager
        )

        binding.viewpager2.adapter = adapter


        return binding.root
    }
    private fun onBoardingFinished(): Boolean{
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }


}