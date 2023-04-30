package com.example.ECommerceApp.data.source.RemoteData

import com.example.ECommerceApp.data.model.BrainShopResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatApiInterface {
    @GET("/get")
    suspend fun askChatBot(
        @Query("bid") bid: String,
        @Query("key") key: String,
        @Query("uid") uid: String,
        @Query("msg") msg: String,
    ): Response<BrainShopResponse>

}