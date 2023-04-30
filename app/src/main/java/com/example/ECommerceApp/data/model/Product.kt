package com.example.ECommerceApp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    val brand: String,
    val category: String,
    val description: String,
    val discountPercentage: Double,
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val images: List<String>,
    val price: Int,
    val rating: Double,
    var stock: Int,
    val thumbnail: String,
    val title: String,
)