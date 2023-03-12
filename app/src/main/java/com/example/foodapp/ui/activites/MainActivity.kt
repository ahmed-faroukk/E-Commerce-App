package com.example.foodapp.ui.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.R
import com.example.foodapp.Repository.Repository
import com.example.foodapp.network.connectivityObserver.ConnectivityObserver
import com.example.foodapp.network.connectivityObserver.NetworkConnectivityObserver
import com.example.foodapp.ui.fragments.home.homeViewModel
import com.example.foodapp.ui.fragments.userAuthentication.LoginViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {
    lateinit var LoginviewModel: LoginViewModel
    lateinit var homeViewModel: homeViewModel
    lateinit var connectivityObserver: ConnectivityObserver
    lateinit var repo : Repository
    var lost : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        repo = Repository()
        LoginviewModel = LoginViewModel(repo, application)
        homeViewModel = homeViewModel(repo , application)
        checkNetwork()


    }
    fun checkNetwork() {

        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        connectivityObserver.observe().onEach { it ->
            if (it.toString() == "Lost" || it.toString() == "Losing" || it.toString() == "Unavailable") {
                Toast.makeText(applicationContext , "lost Connection" , Toast.LENGTH_LONG).show()
                lost = true
            } else {
                if (lost)
                Toast.makeText(applicationContext , "back online" , Toast.LENGTH_LONG).show()
            }
        }.launchIn(lifecycleScope)
    }


}