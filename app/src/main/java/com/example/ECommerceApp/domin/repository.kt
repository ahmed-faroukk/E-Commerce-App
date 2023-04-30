package com.example.ECommerceApp.domin

import androidx.lifecycle.LiveData
import com.example.ECommerceApp.data.model.*
import retrofit2.Response

interface repository {

    suspend fun signIn(loginRequest: LoginRequest): Response<signInResponse>

    suspend fun getProducts(): Response<getAllProducts>

    suspend fun searchProducts(query : String): Response<getAllProducts>

    suspend fun getCategories(): Response<categories>

    suspend fun sendCart(addCartRequest: AddCartRequest) : Response<CartResponse>

    suspend fun upsert(product: Product): Long

    suspend fun delete(product: Product)

    fun getAllProduct(): LiveData<List<Product>>

    fun getTotalPrice(): LiveData<Double>

    suspend fun deleteAll()

    suspend fun askBot(bid : String , key:String , uid : String , msg :String): Response<BrainShopResponse>

}