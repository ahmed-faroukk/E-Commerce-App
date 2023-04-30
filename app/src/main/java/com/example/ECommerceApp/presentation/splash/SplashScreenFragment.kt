package com.example.ECommerceApp.presentation.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ECommerceApp.R
import kotlinx.coroutines.delay

class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            delay(2000)
            findNavController().navigate(R.id.action_splashScreenFragment_to_viewPagerFragment)
        }
    }
}