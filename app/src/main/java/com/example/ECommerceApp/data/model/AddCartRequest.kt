package com.example.ECommerceApp.data.model

data class AddCartRequest(
    val userId: Int,
    val products: List<ProductById>
)
