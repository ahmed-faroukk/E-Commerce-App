package com.example.ECommerceApp.presentation.Home

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.example.ECommerceApp.R
import com.example.ECommerceApp.common.util.Constants.Companion.SEARCH_FOOD_TIME_DELAY
import com.example.ECommerceApp.common.util.Resource
import com.example.ECommerceApp.common.util.gone
import com.example.ECommerceApp.common.util.showSnackbar
import com.example.ECommerceApp.common.util.visible
import com.example.ECommerceApp.common.viewBinding
import com.example.ECommerceApp.databinding.FragmentHomeBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@AndroidEntryPoint
class home : Fragment(R.layout.fragment_home) {

    val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: homeViewModel by activityViewModels()
    lateinit var foodAdapter: FoodAdapter
    lateinit var mail: TextView
    lateinit var img: ImageView
    lateinit var username: TextView
    private var userId: Int = 0
    private lateinit var layoutManager_: LayoutManager


    override fun onDestroyView() {
        super.onDestroyView()

          }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager_ = GridLayoutManager(activity, 2)
        foodAdapter = FoodAdapter()
        initObservation()
        setupRecyclerView()

        binding.imageView3.setOnClickListener {
            it.showSnackbar("clicked")

        }

        // Inflate the layout for this fragment
        viewModel.getAllProducts()
        setupRecyclerView()
        showAllProductsBtn()
        getProducts()
        showDrawerBar()
        val navigationView: NavigationView = binding.root.findViewById(R.id.navigation_view)
        val header: View = navigationView.getHeaderView(0)
        img = header.findViewById(R.id.nav_img)
        username = header.findViewById(R.id.nav_username)
        mail = header.findViewById(R.id.nav_mail)
        getUserData()


        val currentNightMode = requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        with(binding) {
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    // Light mode
                    view2.setBackgroundResource(R.drawable.rectangle_24)
                    ETSearch.setBackgroundResource(R.drawable.rectangle_24)

                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    // Dark mode
                    view2.setBackgroundResource(R.drawable.rectangle_26)
                    ETSearch.setBackgroundResource(R.drawable.rectangle_25)

                }
            }


            // logout
            logoutBtn.setOnClickListener {
                logout()
            }
        }


    }


    fun showAllProductsBtn() {
        with(binding) {
            ResturantBtn.setBackgroundResource(R.drawable.second_login)
            ResturantBtn.setTextColor(Color.parseColor("#FE724C"))
            foodItemBtn.setTextColor(Color.parseColor("#FAFAFA"))
            foodItemBtn.setBackgroundResource(R.drawable.login_btn_bg)
        }
    }

    private fun showDrawerBar() {
        // show drawerBar
        binding.menuBtn.setOnClickListener {
            binding.run { drawerLayout.openDrawer(navigationView) }
        }
    }

    fun showSearchBtn() {
        with(binding){
            foodItemBtn.setBackgroundResource(R.drawable.second_login)
            ResturantBtn.setBackgroundResource(R.drawable.login_btn_bg)
            foodItemBtn.setTextColor(Color.parseColor("#FE724C"))
            ResturantBtn.setTextColor(Color.parseColor("#FAFAFA"))
        }
    }

    private fun setupRecyclerView() {
        binding.HomeRV.apply {
            //set adapter
            adapter = foodAdapter
            //set layoutManager
            this.layoutManager = layoutManager_

        }
        // recyclerView Item Clicked
        foodAdapter.OnItemClickListener {
            viewModel.setRecyclerState(binding.HomeRV.layoutManager?.onSaveInstanceState()!!)
            viewModel.setProduct(it)
            viewModel.setPostion(foodAdapter.differ.currentList.indexOf(it))
            findNavController().navigate(R.id.action_home2_to_showProductFragment)


        }

    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun initObservation() {
        with(binding) {
            with(viewModel) {
                // get products from search
                searchState.observe(viewLifecycleOwner, Observer { productsResponse ->
                    when (productsResponse) {
                        is Resource.Success -> {
                            productsResponse.data?.let {
                                foodAdapter.differ.submitList(it.products)
                                setresultNumber(foodAdapter.itemCount)

                            }
                        }
                        is Resource.Error -> {
                            productsResponse.message?.let {
                                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                            }

                        }

                        else -> {

                        }
                    }


                })
                // get all products
                state.observe(viewLifecycleOwner, Observer { productsResponse ->
                    when (productsResponse) {

                        is Resource.Success -> {
                            productsResponse.data?.let {
                                shimmerLayout.stopShimmer()
                                shimmerLayout.gone()
                                foodAdapter.differ.submitList(it.products)
                                setresultNumber(foodAdapter.itemCount)

                            }
                        }

                        is Resource.Error -> {
                            shimmerLayout.stopShimmer()
                            shimmerLayout.gone()
                            productsResponse.message?.let {
                                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
                            }

                        }

                        is Resource.Loading -> {
                            shimmerLayout.visible()
                            shimmerLayout.startShimmer()

                        }

                        else -> {}
                    }


                })

                // get result number of products
                resultsNumber.observe(viewLifecycleOwner, Observer {

                    val language = resources.configuration.locales.get(0).language
                    if (language == "ar") {
                        resultNum.text = " وجد ${it} منتج "

                    } else {
                        resultNum.text = "Found ${it} results"
                    }

                })


                getTotalPrice().observe(viewLifecycleOwner, Observer {
                    navigationView.setNavigationItemSelectedListener { item ->
                        when (item.itemId) {
                            R.id.order -> {
                                if (it == null)
                                    findNavController().navigate(R.id.action_home_to_emptyCartFragment)
                                else
                                    findNavController().navigate(R.id.action_home_to_cartFragment)
                            }
                            R.id.setting -> {
                                findNavController().navigate(R.id.action_home_to_appSittingFragment)
                            }
                            R.id.helps -> {
                                findNavController().navigate(R.id.action_home_to_chatBotFragment)
                            }

                        }
                        drawerLayout.closeDrawer(GravityCompat.START)
                        true
                    }
                })
            }
        }


    }

    private fun getProducts() {
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
    private fun getUserData() {
        val instanse = requireContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        Glide.with(this).load(instanse.getString("image", "").toString()).into(img)
        Glide.with(this).load(instanse.getString("image", "").toString()).into(binding.imageView3)
        username.text = instanse.getString("username", "").toString()
        mail.text = instanse.getString("password", "").toString()
        userId = instanse.getInt("id", 0)
        viewModel.setUserId(userId)
    }

    private fun logout() {
        val instance = requireContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val editor = instance.edit()
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

}


