package com.example.foodapp.ui.fragments.userAuthentication

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.view.ViewDebug.IntToString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodapp.Repository.RepositoryImp
import com.example.foodapp.models.LoginRequest
import com.example.foodapp.models.signInResponse
import com.example.foodapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

// using android viewModel For Context aware

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: RepositoryImp,
    app: Application,
) : AndroidViewModel(app) {

    @SuppressLint("StaticFieldLeak")
    val context: Context = getApplication<Application>().applicationContext

    // livedata
    val state = MutableLiveData<Resource<signInResponse>>()


    fun signIn(loginRequest: LoginRequest) {
        viewModelScope.launch {
            try {
                state.postValue(Resource.Loading())
                val response = repo.signIn(loginRequest)
                state.postValue(handleSignIn(response))
            } catch (t: Throwable) {
                Resource.Error(t.message, null)
            }
        }
    }

    fun handleSignIn(response: Response<signInResponse>)
            : Resource<signInResponse> {
        if (response.isSuccessful) {
            // make sure that body not null
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message(), null)
    }

}