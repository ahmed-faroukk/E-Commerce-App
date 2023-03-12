package com.example.foodapp.ui.fragments.home

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodapp.Repository.Repository
import com.example.foodapp.models.Product
import com.example.foodapp.models.getAllProducts
import com.example.foodapp.models.signInResponse
import com.example.foodapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class homeViewModel(val repo: Repository, app: Application) : AndroidViewModel(app) {
    @SuppressLint("StaticFieldLeak")
    val context: Context = getApplication<Application>().applicationContext



    //live data
    val state = MutableLiveData<Resource<getAllProducts>>()
    val searchState = MutableLiveData<Resource<getAllProducts>>()
    val resultsNumber = MutableLiveData<Int>()
    val RecyclerviewState = MutableLiveData<Parcelable>()

    //send data between home and productDetails
    var product = MutableLiveData<Product>()

// get all products
    fun getAllProducts(){
        viewModelScope.launch {
            try {
                val response = repo.getProducts()
                state.postValue(getProductHandling(response))
            }catch (t : Throwable){
                Resource.Error(t.message , null)
            }

        }
    }

    fun getProductHandling(response: Response<getAllProducts>) : Resource<getAllProducts>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message() , null)
    }





    // search for product
    fun searchProduct(q : String){
        viewModelScope.launch {
            try {
                val response = repo.searchProducts(q)
                searchState.postValue(handleSearch(response))

            }catch (t: Throwable){
                Resource.Error(t.message , null)
            }
        }
    }
    fun handleSearch(response: Response<getAllProducts>) : Resource<getAllProducts>{
        if (response.isSuccessful)
        {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message() , null)
    }


    // setters

    fun setresultNumber(number : Int){
        resultsNumber.postValue(number)
    }
    fun setProduct(product: Product){
        this.product.postValue(product)
    }
    fun setRecyclerState(state :  Parcelable){
        RecyclerviewState.postValue(state)
    }
}