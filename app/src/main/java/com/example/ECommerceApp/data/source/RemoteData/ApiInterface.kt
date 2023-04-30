package com.example.ECommerceApp.data.source.RemoteData

import com.example.ECommerceApp.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @Headers("Content-Type: application/json")
    @POST("/auth/login")
    suspend fun signIn(@Body request: LoginRequest): Response<signInResponse>

    @GET("/products")
    suspend fun getFood(): Response<getAllProducts>

    @GET("/products/search")
    suspend fun searchProducts(@Query("q") query: String): Response<getAllProducts>

    @GET("products/categories")
    suspend fun getCategories(): Response<categories>

    @POST("carts/add")
    suspend fun addCart(@Body request: AddCartRequest): Response<CartResponse>



}