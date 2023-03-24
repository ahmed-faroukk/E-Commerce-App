package com.example.foodapp.ui.fragments.Home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.foodapp.databinding.FragmentShowProductBinding
import com.example.foodapp.R
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    lateinit var binding: FragmentShowProductBinding
    private val viewModel: homeViewModel by viewModels()
    var productNumber = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {


        binding = FragmentShowProductBinding.inflate(inflater, container, false)


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