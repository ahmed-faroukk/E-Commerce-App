package com.example.foodapp.domin

import com.example.foodapp.models.LoginRequest
import com.example.foodapp.models.categories
import com.example.foodapp.models.getAllProducts
import com.example.foodapp.models.signInResponse
import retrofit2.Response

interface repository {

    suspend fun signIn(loginRequest: LoginRequest): Response<signInResponse>

    suspend fun getProducts(): Response<getAllProducts>

    suspend fun searchProducts(query : String): Response<getAllProducts>

    suspend fun getCategories(): Response<categories>

}