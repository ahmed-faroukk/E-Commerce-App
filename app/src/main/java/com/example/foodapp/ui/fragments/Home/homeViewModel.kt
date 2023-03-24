package com.example.foodapp.ui.fragments.Home

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodapp.Repository.RepositoryImp
import com.example.foodapp.models.Product
import com.example.foodapp.models.categories
import com.example.foodapp.models.getAllProducts
import com.example.foodapp.util.Resource
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

    //send data between home and productDetails
    var product = MutableLiveData<Product>()

    // get all products
    fun getAllProducts() {
        viewModelScope.launch {
            try {
                val response = repo.getProducts()
                state.postValue(getProductHandling(response))
            } catch (t: Throwable) {
                Resource.Error(t.message, null)
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
            try {
                val response = repo.searchProducts(q)
                searchState.postValue(handleSearch(response))

            } catch (t: Throwable) {
                Resource.Error(t.message, null)
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

    fun setRecyclerState(state: Parcelable) {
        RecyclerviewState.postValue(state)
    }
}