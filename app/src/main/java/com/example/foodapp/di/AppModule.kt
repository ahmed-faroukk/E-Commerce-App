package com.example.foodapp.di

import android.app.Application
import com.example.foodapp.Repository.RepositoryImp
import com.example.foodapp.data.RemoteData.ApiInterface
import com.example.foodapp.domin.repository
import com.example.foodapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMyApi() : ApiInterface{
         val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging).build()

            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
            return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api : ApiInterface , app : Application) : repository {
        return RepositoryImp(api , app)
    }
}