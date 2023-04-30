package com.example.ECommerceApp.di

import android.content.Context
import com.example.ECommerceApp.common.util.Constants
import com.example.ECommerceApp.data.source.Database.ProductDatabase
import com.example.ECommerceApp.data.source.RemoteData.ApiInterface
import com.example.ECommerceApp.data.source.RemoteData.ChatApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideMyApi(): ApiInterface {
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
    fun provideMyChatApi(): ChatApiInterface {
        val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging).build()

            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_AI)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        return retrofit.create(ChatApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideProductDatabase(
        @ApplicationContext app: Context,
    ): ProductDatabase {
        return ProductDatabase.invoke(app)
    }



}