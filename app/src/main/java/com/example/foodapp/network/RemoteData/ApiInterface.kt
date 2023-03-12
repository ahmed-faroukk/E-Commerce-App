package com.example.foodapp.network.RemoteData

import com.example.foodapp.models.LoginRequest
import com.example.foodapp.models.getAllProducts
import com.example.foodapp.models.signInResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @Headers("Content-Type: application/json")
    @POST("/auth/login")
    suspend fun signIn(@Body request : LoginRequest) : Response<signInResponse>

    @GET("/products")
    suspend fun getFood() : Response<getAllProducts>

    @GET("/products/search")
    suspend fun searchProducts(@Query("q") query: String): Response<getAllProducts>

}