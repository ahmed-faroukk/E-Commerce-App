package com.example.foodapp.ui.fragments.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.foodapp.databinding.FragmentShowProductBinding
import com.example.foodapp.ui.activites.MainActivity
import com.example.foodapp.R
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar


class ProductDetailsFragment : Fragment() {
    lateinit var binding: FragmentShowProductBinding
    lateinit var viewModel: homeViewModel
    var productNumber = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {


        binding = FragmentShowProductBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).homeViewModel


        // get product data in my layout
        viewModel.product.observe(viewLifecycleOwner, Observer {
            Glide.with(this)
                .load(it.thumbnail)
                .into(binding.productImg)
            binding.productTitle.text = it.title
            binding.productRate.text = it.rating.toString()
            binding.productNumber.text = "(+${it.stock})"
            binding.productPrice.text = it.price.toString()
            binding.productDescription.text = it.description

        })

        // minus Button
        binding.productMinusBtn.setOnClickListener {
           handleMinusProduct()
        }

        // add button
        binding.productAddBtn.setOnClickListener {
           handleAddProduct()
        }

        // back Button
        binding.productBack.setOnClickListener {
            findNavController().navigate(R.id.action_showProductFragment_to_home2)
        }

            // add to cart ui only
        binding.productAddTocart.setOnClickListener {
           customSnackBar()
        }


        return binding.root
    }

    fun handleAddProduct(){
        productNumber++
        binding.productHowManyAddedTv.text = productNumber.toString()
    }
    fun handleMinusProduct(){
        if (productNumber > 0)
            productNumber--
        binding.productHowManyAddedTv.text = productNumber.toString()
    }
    fun customSnackBar(){
        val snak = Snackbar.make(binding.root, "added successfully", Snackbar.LENGTH_LONG)
            .setAction("action", null)
        val sbView = snak.view
        sbView.setBackgroundColor(ContextCompat.getColor(requireActivity() , R.color.sb))
        snak.show()
    }

}