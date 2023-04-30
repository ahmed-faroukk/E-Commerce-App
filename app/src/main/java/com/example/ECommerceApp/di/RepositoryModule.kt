package com.example.ECommerceApp.di

import android.app.Application
import com.example.ECommerceApp.data.Repository.RepositoryImp
import com.example.ECommerceApp.data.source.Database.ProductDatabase
import com.example.ECommerceApp.data.source.RemoteData.ApiInterface
import com.example.ECommerceApp.data.source.RemoteData.ChatApiInterface
import com.example.ECommerceApp.domin.repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(api: ApiInterface, chatApi : ChatApiInterface, app: Application, db : ProductDatabase): repository {
        return RepositoryImp(api, chatApi ,app , db)
    }


}