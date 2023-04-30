package com.example.ECommerceApp.presentation.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ECommerceApp.data.Repository.RepositoryImp
import com.example.ECommerceApp.data.model.AddCartRequest
import com.example.ECommerceApp.data.model.CartResponse
import com.example.ECommerceApp.data.model.Product
import com.example.ECommerceApp.common.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: RepositoryImp,
    private val app: Application,
) : AndroidViewModel(app) {

    val responseState = MutableLiveData<Resource<CartResponse>>()

    fun getCart() = repository.getAllProduct()

    fun getTotalPrice() = repository.getTotalPrice()

    fun AddToCart(product: Product) = viewModelScope.launch {
        repository.upsert(product)
    }

    fun deleteProduct(product: Product) = viewModelScope.launch {
        repository.delete(product)
    }

    fun deleteAllProducts() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun sendCartTOServer(addCartRequest: AddCartRequest) = viewModelScope.launch {
        try {
            responseState.postValue(Resource.Loading())
            val response = repository.sendCart(addCartRequest)
            responseState.postValue(handleSendCart(response))
        } catch (t: Throwable) {
            responseState.postValue(Resource.Error(t.message, null))
        }
    }

    fun handleSendCart(response: Response<CartResponse>): Resource<CartResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), data = null)
    }


}