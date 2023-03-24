package com.example.foodapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.foodapp.R
import com.example.foodapp.Repository.RepositoryImp
import com.example.foodapp.network.connectivityObserver.ConnectivityObserver
import com.example.foodapp.network.connectivityObserver.NetworkConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var connectivityObserver: ConnectivityObserver
    lateinit var repo : RepositoryImp
    var lost : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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