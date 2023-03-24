package com.example.foodapp.Repository

import android.app.Application
import com.example.foodapp.data.RemoteData.ApiInterface
import com.example.foodapp.domin.repository
import com.example.foodapp.models.LoginRequest
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val apiInterface :ApiInterface ,
    private val app : Application
    ): repository {

    override suspend fun signIn(loginRequest: LoginRequest)
    = apiInterface.signIn(loginRequest)


    override suspend fun getProducts() =
        apiInterface.getFood()


    override suspend fun searchProducts(query: String) =
        apiInterface.searchProducts(query)


    override suspend fun getCategories() =
        apiInterface.getCategories()

}