package com.example.ECommerceApp.presentation.Home

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ECommerceApp.data.Repository.RepositoryImp
import com.example.ECommerceApp.data.model.Product
import com.example.ECommerceApp.data.model.categories
import com.example.ECommerceApp.data.model.getAllProducts
import com.example.ECommerceApp.common.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class homeViewModel @Inject constructor(
    private val repo: RepositoryImp,
    private val app: Application,
) : AndroidViewModel(app) {


    //live data
    val state = MutableLiveData<Resource<getAllProducts>>()
    val categories = MutableLiveData<Resource<categories>>()
    val searchState = MutableLiveData<Resource<getAllProducts>>()
    val resultsNumber = MutableLiveData<Int>()
    val RecyclerviewState = MutableLiveData<Parcelable>()
    val RecyclerviewPos = MutableLiveData<Int>()


    //send data between home and productDetails
    var product = MutableLiveData<Product>()
    var userId = MutableLiveData<Int>()


    // get all products
    fun getAllProducts() {
        viewModelScope.launch {
            try {
                state.postValue(Resource.Loading())
                val response = repo.getProducts()
                state.postValue(getProductHandling(response))
            } catch (t: Throwable) {
                state.postValue(Resource.Error(t.message, null))
            }

        }
    }

    fun getProductHandling(response: Response<getAllProducts>): Resource<getAllProducts> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }


    // search for product
    fun searchProduct(q: String) {
        viewModelScope.launch {
            searchState.postValue(Resource.Loading())
            try {
                val response = repo.searchProducts(q)
                searchState.postValue(handleSearch(response))

            } catch (t: Throwable) {
               searchState.postValue(Resource.Error(t.message, null))
            }
        }
    }

    fun handleSearch(response: Response<getAllProducts>): Resource<getAllProducts> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }

    fun getCategories() {
        try {
            viewModelScope.launch {
                val response = repo.getCategories()
                handleCategories(response)

            }
        } catch (t: Throwable) {
            Resource.Error(t.message, null)
        }
    }

    fun handleCategories(response: Response<categories>): Resource<categories> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }


    // setters

    fun setresultNumber(number: Int) {
        resultsNumber.postValue(number)
    }

    fun setProduct(product: Product) {
        this.product.postValue(product)
    }
    fun setUserId(id : Int) {
        this.userId.postValue(id)
    }

    fun setRecyclerState(state: Parcelable) {
        RecyclerviewState.postValue(state)
    }
    fun setPostion(pos : Int){
       RecyclerviewPos.postValue(pos)
    }

     fun getTotalPrice() = repo.getTotalPrice()

    // cart
    fun AddToCart(product: Product) = viewModelScope.launch {
        repo.upsert(product)
    }

}