package com.example.ECommerceApp.data.model

data class getAllProducts(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)