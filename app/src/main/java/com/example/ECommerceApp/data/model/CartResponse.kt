package com.example.ECommerceApp.data.model

data class CartResponse(
    val discountedTotal: Int,
    val id: Int,
    val products: List<ProductX>,
    val total: Int,
    val totalProducts: Int,
    val totalQuantity: Int,
    val userId: Int
)