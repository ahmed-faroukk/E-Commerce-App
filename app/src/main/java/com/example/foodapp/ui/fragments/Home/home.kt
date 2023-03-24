package com.example.foodapp.ui.fragments.Home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.adpters.FoodAdapter
import com.example.foodapp.databinding.FragmentHomeBinding
import com.example.foodapp.util.Constants.Companion.SEARCH_FOOD_TIME_DELAY
import com.example.foodapp.util.Resource
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class home : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val viewModel: homeViewModel by viewModels()
    lateinit var foodAdapter: FoodAdapter
    lateinit var mail: TextView
    lateinit var img: ImageView
    lateinit var username: TextView

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
        viewModel.getAllProducts()
        foodAdapter = FoodAdapter()
        setupRecyclerView()
        showAllProductsBtn()
        Observation()
        getProducts()
        showDrawerBar()
        val navigationView : NavigationView = binding.root.findViewById(R.id.navigation_view);
        val header : View = navigationView.getHeaderView(0)
        img = header.findViewById(R.id.nav_img)
        username = header.findViewById(R.id.nav_username)
        mail = header.findViewById(R.id.nav_mail)
        getUserData()

        // logout
        binding.logoutBtn.setOnClickListener {
            logout()
        }


        return binding.root
    }


    fun showAllProductsBtn() {
        binding.ResturantBtn.setBackgroundResource(R.drawable.second_login)
        binding.ResturantBtn.setTextColor(Color.parseColor("#FE724C"))
        binding.foodItemBtn.setTextColor(Color.parseColor("#FAFAFA"))
        binding.foodItemBtn.setBackgroundResource(R.drawable.login_btn_bg)

    }

    fun showDrawerBar() {
        // show drawerBar
        binding.menuBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navigationView)
        }
    }

    fun showSearchBtn() {

        binding.foodItemBtn.setBackgroundResource(R.drawable.second_login)
        binding.ResturantBtn.setBackgroundResource(R.drawable.login_btn_bg)
        binding.foodItemBtn.setTextColor(Color.parseColor("#FE724C"))
        binding.ResturantBtn.setTextColor(Color.parseColor("#FAFAFA"))

    }

    private fun setupRecyclerView() {
        foodAdapter = FoodAdapter()
        binding.HomeRV.apply {
            //set adapter
            adapter = foodAdapter
            //set layoutManager
            layoutManager = GridLayoutManager(activity, 2)
        }

        // recyclerView Item Clicked
        foodAdapter.OnItemClickListener {
            findNavController().navigate(R.id.action_home2_to_showProductFragment)
            viewModel.setProduct(it)
        }

    }


    fun logout() {
        val instanse = requireContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val editor = instanse.edit()
        editor.remove("username")
        editor.remove("password")
        editor.remove("User_logIn")
        editor.remove("email")
        editor.remove("gender")
        editor.remove("image")
        editor.remove("token")
        editor.apply()
        findNavController().navigate(R.id.action_home_to_loginFragment)
    }


    fun Observation() {
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


        })

        // get result number of products
        viewModel.resultsNumber.observe(viewLifecycleOwner, Observer {

            binding.resultNum.text = "Found ${it} results"

        })

        viewModel.categories.observe(viewLifecycleOwner, Observer {

        })

    }

    fun getProducts() {
        // send search request
        binding.ETSearch.addTextChangedListener {
            // job reduce requests
            var job: Job? = null
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_FOOD_TIME_DELAY)
            }
            it?.let {
                if (it.toString().isNotEmpty()) {
                    viewModel.searchProduct(it.toString())
                    showSearchBtn()
                } else {
                    viewModel.getAllProducts()
                    showAllProductsBtn()
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    fun getUserData() {
        val instanse = requireContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        Glide.with(this).load(instanse.getString("image" , "").toString()).into(img)
        Glide.with(this).load(instanse.getString("image" , "").toString()).into(binding.imageView3)
        username.text = instanse.getString("username", "").toString()
        mail.text = instanse.getString("password", "").toString()
    }

}


