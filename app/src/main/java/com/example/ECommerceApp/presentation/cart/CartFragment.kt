package com.example.ECommerceApp.presentation.cart

import android.content.Context
import android.content.res.Configuration
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ECommerceApp.R
import com.example.ECommerceApp.presentation.Home.FoodAdapter
import com.example.ECommerceApp.databinding.FragmentCartBinding
import com.example.ECommerceApp.data.model.AddCartRequest
import com.example.ECommerceApp.data.model.ProductById
import com.example.ECommerceApp.presentation.Home.homeViewModel
import com.example.ECommerceApp.common.util.NotificationUtil
import com.example.ECommerceApp.common.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart), LocationHelper.OnLocationFetchedListener {
    private val binding: FragmentCartBinding by viewBinding(FragmentCartBinding::bind)
    val viewModel: CartViewModel by viewModels()
    private val sharedviewModel: homeViewModel by activityViewModels()
    lateinit var foodAdapter: FoodAdapter
    private val listOfProducts = mutableListOf<ProductById>()
    private var UserId = 0
    private lateinit var locationHelper: LocationHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(0)
        locationHelper = LocationHelper(requireActivity() as AppCompatActivity)
        val bottomSheet = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.crdeit_card_layout, null)
        val CrdeitCancelBtn = view.findViewById<Button>(R.id.crdeit_cancelBtn)
        val CrdeitPayBtn = view.findViewById<Button>(R.id.crdeit_payBtn)
        val preferences =
            requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val allowNotification = preferences.getBoolean("notification", true)


        val currentNightMode =
            requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                // Light mode
                binding.cartlaout.setBackgroundResource(R.color.white)
                binding.cartLocationBtn.setBackgroundResource(R.drawable.login_et_bg)
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                // Dark mode
                binding.cartlaout.setBackgroundResource(R.color.white_darkMood)
                binding.cartLocationBtn.setBackgroundResource(R.drawable.rectangle_25)
            }
        }

        CrdeitCancelBtn.setOnClickListener {
            binding.CartPaymentBtn.clearCheck()
            bottomSheet.dismiss()
        }
        sharedviewModel.userId.observe(viewLifecycleOwner, Observer {
            UserId = it
        })

        binding.cartCheckout.setOnClickListener {

            val dialogView =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog, null)
            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            dialog.show()

            val confirmButton = dialogView.findViewById<Button>(R.id.confirmBtn)
            confirmButton.setOnClickListener {
                findNavController().navigate(R.id.action_cartFragment_to_orederCompletedFragment)
                dialog.dismiss()
                val cartRequest = AddCartRequest(UserId, listOfProducts)
                viewModel.sendCartTOServer(cartRequest)
                if (allowNotification)
                    NotificationUtil.sendNotification(requireContext(),
                        "e commerce app",
                        "wait a call from our customer service")

            }
            val cancelButton = dialogView.findViewById<Button>(R.id.cancelBtn)
            cancelButton.setOnClickListener {
                // Perform action here
                dialog.dismiss()
            }


        }



        binding.cartLocationBtn.setOnClickListener {
            binding.cartLocationBtn.text = "Click again"
            locationHelper.fetchLocation(this)
        }

        binding.CartPaymentBtn.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.option1 -> {

                    bottomSheet.setContentView(view)
                    bottomSheet.setCancelable(false)
                    bottomSheet.show()


                }
            }
        }

        viewModel.getTotalPrice().observe(viewLifecycleOwner, Observer {
            var reciveNum = it
            if (reciveNum != null) {
                binding.cartSubTotal.text = reciveNum.toString()
                var total = reciveNum + 10
                binding.cartTotal.text = total.toString()

            } else {
                findNavController().navigate(R.id.action_cartFragment_to_emptyCartFragment)
            }

        })

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.UP or ItemTouchHelper.DOWN
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                // detect product position
                val position = viewHolder.adapterPosition
                val product = foodAdapter.differ.currentList[position]
                viewModel.deleteProduct(product)
                Snackbar.make(binding.root,
                    "Successfully deleted product from cart ",
                    Snackbar.LENGTH_LONG).apply {
                    setAction("undo") {
                        viewModel.AddToCart(product)
                    }
                    show()
                }

            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {

                return 0.2f
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.cartRv)
        }

        viewModel.getCart().observe(viewLifecycleOwner, Observer {
            foodAdapter.differ.submitList(it)
            for (product in it) {

                val product = ProductById(
                    product.id,
                    1
                )
                listOfProducts.add(product)

            }

        })


    }


    private fun setupRecyclerView(x: Int) {
        foodAdapter = FoodAdapter()
        binding.cartRv.apply {
            //set adapter
            adapter = foodAdapter
            //set layoutManager
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

            scrollToPosition(x)
        }

        // recyclerView Item Clicked
        foodAdapter.OnItemClickListener {
            sharedviewModel.setProduct(it)
            findNavController().navigate(R.id.action_cartFragment_to_showProductFragment)
        }

    }

    override fun onLocationFetched(location: Location?, address: String?) {
        binding.cartLocationBtn.text = address
    }

    override fun onError(error: String?) {
        binding.cartLocationBtn.text = error
    }


}