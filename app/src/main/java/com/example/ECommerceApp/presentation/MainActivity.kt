package com.example.ECommerceApp.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.ECommerceApp.R
import com.example.ECommerceApp.data.Repository.RepositoryImp
import com.example.ECommerceApp.network.connectivityObserver.ConnectivityObserver
import com.example.ECommerceApp.network.connectivityObserver.NetworkConnectivityObserver
import com.example.ECommerceApp.presentation.Home.homeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var connectivityObserver: ConnectivityObserver
    lateinit var repo : RepositoryImp
    val viewmodel : homeViewModel by viewModels()
    var lost : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkNetwork()
        val preferences = applicationContext.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val currentLanguage = preferences.getString("language", "en")
        val currentThemeIs = preferences.getString("mood" , "l")
        if (currentThemeIs == "d"){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

            setLocale(currentLanguage.toString())






    }
    @SuppressLint("SuspiciousIndentation")
    fun checkNetwork() {


        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        connectivityObserver.observe().onEach { it ->
            if (it.toString() == "Lost" || it.toString() == "Losing" || it.toString() == "Unavailable") {
                //start destination
                Toast.makeText(applicationContext , "lost Connection" , Toast.LENGTH_LONG).show()
                lost = true

            } else {
                if (lost)
                Toast.makeText(applicationContext , "back online" , Toast.LENGTH_LONG).show()

            }
        }.launchIn(lifecycleScope)
    }

    fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        this.resources.updateConfiguration(config, this.resources.displayMetrics)
    }


}