package com.example.foodapp.Repository

import com.example.foodapp.network.RemoteData.RetrofitInstance
import com.example.foodapp.models.LoginRequest

class Repository {
    suspend fun signIn(loginRequest: LoginRequest) =
        RetrofitInstance().api.signIn(loginRequest)

    suspend fun getProducts() = RetrofitInstance().api.getFood()

    suspend fun searchProducts(query : String) = RetrofitInstance().api.searchProducts(query)


}