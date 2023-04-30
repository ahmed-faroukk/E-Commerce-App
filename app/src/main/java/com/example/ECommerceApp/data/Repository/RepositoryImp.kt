package com.example.ECommerceApp.data.Repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.ECommerceApp.data.model.*
import com.example.ECommerceApp.data.source.Database.ProductDatabase
import com.example.ECommerceApp.data.source.RemoteData.ApiInterface
import com.example.ECommerceApp.data.source.RemoteData.ChatApiInterface
import com.example.ECommerceApp.domin.repository

import retrofit2.Response
import javax.inject.Inject

class RepositoryImp @Inject constructor(
    private val apiInterface: ApiInterface,
    private val chatApi : ChatApiInterface,
    private val app: Application,
    private val db: ProductDatabase
) : repository {

    override suspend fun signIn(loginRequest: LoginRequest) = apiInterface.signIn(loginRequest)


    override suspend fun getProducts() =
        apiInterface.getFood()


    override suspend fun searchProducts(query: String) =
        apiInterface.searchProducts(query)


    override suspend fun getCategories() =
        apiInterface.getCategories()

    override suspend fun sendCart(addCartRequest: AddCartRequest): Response<CartResponse> =
        apiInterface.addCart(addCartRequest)

    override suspend fun askBot(bid : String , key:String , uid : String , msg :String) : Response<BrainShopResponse> =  chatApi.askChatBot(bid,key,uid ,msg)




        //room imp

    override suspend fun upsert(product: Product) = db.getProductDao().upsert(product)

    override suspend fun delete(product: Product) = db.getProductDao().deleteProduct(product)

    override fun getAllProduct() = db.getProductDao().getAllProducts()

    override fun getTotalPrice(): LiveData<Double> = db.getProductDao().getTotalPrice()

    override suspend fun deleteAll() = db.getProductDao().deleteAll()


}