package com.example.ECommerceApp.presentation.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.ECommerceApp.R
import com.example.ECommerceApp.common.viewBinding
import com.example.ECommerceApp.databinding.FragmentEmptyCartBinding

class EmptyCartFragment : Fragment(R.layout.fragment_empty_cart) {
    private val binding : FragmentEmptyCartBinding by viewBinding(FragmentEmptyCartBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.emptyCartShopNow.setOnClickListener {
            findNavController().navigate(R.id.action_emptyCartFragment_to_home)
        }


    }

}