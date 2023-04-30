package com.example.ECommerceApp.presentation.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.ECommerceApp.databinding.FragmentShowProductBinding
import com.example.ECommerceApp.R
import com.bumptech.glide.Glide
import com.example.ECommerceApp.common.viewBinding
import com.example.ECommerceApp.data.model.Product
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_show_product) {

    private val binding: FragmentShowProductBinding by viewBinding(FragmentShowProductBinding::bind)
    private val viewModel: homeViewModel by activityViewModels()
    lateinit var product: Product
    private var productNumber = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get product data in my layout
        viewModel.product.observe(viewLifecycleOwner, Observer {

            setProductt(it)
            Glide.with(this)
                .load(it.thumbnail)
                .into(binding.productImg)
            binding.productTitle.text = it.title
            binding.productRate.text = it.rating.toString()
            binding.productNumber.text = "(+${it.stock})"
            binding.productPrice.text = it.price.toString()
            binding.productDescription.text = it.description
            productNumber = it.stock

        })

        // minus Button
        binding.productMinusBtn.setOnClickListener {
            handleMinusProduct()
        }

        // add button
        binding.productAddBtn.setOnClickListener {
            handleAddProduct()
        }



        // add to cart ui only
        binding.productAddTocart.setOnClickListener {
            //save this products in cart
            viewModel.product.observe(viewLifecycleOwner, Observer {
                Log.d("productFromShowProduct" , it.title)
                viewModel.AddToCart(it)
            })
            customSnackBar()
        }

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
        val snak = Snackbar.make(binding.root, "added successfully", 500)
            .setAction("action", null)
        val sbView = snak.view
        sbView.setBackgroundColor(ContextCompat.getColor(requireActivity() , R.color.select))
        snak.setAnchorView(binding.productAddTocart)
        snak.show()
    }

    fun setProductt(product: Product){
        this.product = product
    }

}