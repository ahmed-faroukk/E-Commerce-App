package com.example.ECommerceApp.presentation.customerService

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ECommerceApp.data.Repository.RepositoryImp
import com.example.ECommerceApp.data.model.BrainShopResponse
import com.example.ECommerceApp.common.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ChatBotViewModel @Inject constructor(
    val repositoryImp: RepositoryImp,
    app: Application,
) : AndroidViewModel(app) {

    val chatBotAnswer = MutableLiveData<Resource<BrainShopResponse>>()

    fun askChatBot(bid : String , key:String , uid : String , msg :String)  = viewModelScope.launch {
        chatBotAnswer.postValue(Resource.Loading())

        try {
           val respose = repositoryImp.askBot(bid , key , uid , msg)
           chatBotAnswer.postValue(handleCharRequest(respose))
        }catch (t : Throwable){
            chatBotAnswer.postValue(Resource.Error(t.message , null))
        }
    }

    fun handleCharRequest( response: Response<BrainShopResponse>) : Resource<BrainShopResponse> {
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message() , data = null)

    }
}