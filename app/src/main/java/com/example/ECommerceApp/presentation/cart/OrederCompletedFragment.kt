package com.example.ECommerceApp.presentation.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ECommerceApp.R
import com.example.ECommerceApp.common.viewBinding
import com.example.ECommerceApp.databinding.FragmentOrederCompletedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrederCompletedFragment : Fragment(R.layout.fragment_oreder_completed) {
    val viewModel: CartViewModel by viewModels()
    private val binding: FragmentOrederCompletedBinding by viewBinding(FragmentOrederCompletedBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.deleteAllProducts()

        binding.ok.setOnClickListener {
            findNavController().navigate(R.id.action_orederCompletedFragment_to_home)
        }


    }

}