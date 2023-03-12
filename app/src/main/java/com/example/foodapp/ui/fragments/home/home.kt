package com.example.foodapp.ui.fragments.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodapp.R
import com.example.foodapp.adpters.FoodAdapter
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.ui.activites.MainActivity
import com.example.foodapp.util.Constants.Companion.SEARCH_FOOD_TIME_DELAY
import com.example.foodapp.util.Resource
import kotlinx.coroutines.*
import org.jetbrains.annotations.NotNull
import java.util.Objects


class home : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: homeViewModel
    lateinit var foodAdapter: FoodAdapter

    // handle recyclerView state
    override fun onPause() {
        super.onPause()

        val state = binding.HomeRV.layoutManager?.onSaveInstanceState()
        viewModel.setRecyclerState(state!!)
    }

    override fun onResume() {
        super.onResume()
        viewModel.RecyclerviewState.observe(viewLifecycleOwner, Observer {
            binding.HomeRV.layoutManager?.onRestoreInstanceState(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).homeViewModel
        viewModel.getAllProducts()
        foodAdapter = FoodAdapter()
        setupRecyclerView()
        handleButtonsTransitions()

        // recyclerView Item Clicked
         foodAdapter.OnItemClickListener {
            findNavController().navigate(R.id.action_home2_to_showProductFragment)
             viewModel.setProduct(it)
        }

        // get result number of products
        viewModel.resultsNumber.observe(viewLifecycleOwner, Observer {

            binding.resultNum.text = "Found ${it} results"

        })

        // get all products
        viewModel.state.observe(viewLifecycleOwner, Observer { productsResponse ->
            when (productsResponse) {

                is Resource.Success -> {
                    productsResponse.data?.let {
                        foodAdapter.differ.submitList(it.products)
                        viewModel.setresultNumber(foodAdapter.itemCount)

                    }
                }

                is Resource.Error -> {
                    productsResponse.message?.let {
                        Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                    }

                }

                else -> {}
            }

           // send search request
            binding.ETSearch.addTextChangedListener {
                // job reduce requests
                var job : Job? = null
                job?.cancel()
                job = MainScope().launch {
                    delay(SEARCH_FOOD_TIME_DELAY)
                }
                it?.let {
                    if(it.toString().isNotEmpty()) {
                        viewModel.searchProduct(it.toString())
                    }else{
                        viewModel.getAllProducts()
                    }
                }
            }


        })

        // get products from search
        viewModel.searchState.observe(viewLifecycleOwner, Observer { productsResponse ->
            when (productsResponse) {
                is Resource.Success -> {
                    productsResponse.data?.let {
                        foodAdapter.differ.submitList(it.products)
                        viewModel.setresultNumber(foodAdapter.itemCount)

                    }
                }
                is Resource.Error -> {
                    productsResponse.message?.let {
                        Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                    }

                }

                else -> {}
            }


        })

        // show drawerBar
        binding.menuBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navigationView)
        }

        // logout
        binding.logoutBtn.setOnClickListener {
            logout()
        }

        return binding.root
    }


    fun handleButtonsTransitions() {
        binding.foodItemBtn.setOnClickListener {
            binding.ResturantBtn.setBackgroundResource(R.drawable.second_login)
            binding.ResturantBtn.setTextColor(Color.parseColor("#FE724C"))
            binding.foodItemBtn.setTextColor(Color.parseColor("#FAFAFA"))
            binding.foodItemBtn.setBackgroundResource(R.drawable.login_btn_bg)

        }
        binding.ResturantBtn.setOnClickListener {
            binding.foodItemBtn.setBackgroundResource(R.drawable.second_login)
            binding.ResturantBtn.setBackgroundResource(R.drawable.login_btn_bg)
            binding.foodItemBtn.setTextColor(Color.parseColor("#FE724C"))
            binding.ResturantBtn.setTextColor(Color.parseColor("#FAFAFA"))
        }
    }

    private fun setupRecyclerView() {
        foodAdapter = FoodAdapter()
        binding.HomeRV.apply {
            //set adapter
            adapter = foodAdapter
            //set layoutManager
            layoutManager = GridLayoutManager(activity, 2)
        }

    }


    fun logout(){
            val instanse = requireContext().getSharedPreferences("login_prefs" , Context.MODE_PRIVATE)
            val editor = instanse.edit()
            editor.remove("username")
            editor.remove("password")
            editor.apply()
        findNavController().navigate(R.id.action_home_to_loginFragment)
    }


}


