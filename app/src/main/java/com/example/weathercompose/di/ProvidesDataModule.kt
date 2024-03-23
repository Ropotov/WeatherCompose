package com.example.weathercompose.di

import android.content.Context
import com.example.weathercompose.data.database.CityDao
import com.example.weathercompose.data.database.CityDatabase
import com.example.weathercompose.data.network.api.ApiFactory
import com.example.weathercompose.data.network.api.ApiService
import dagger.Module
import dagger.Provides

@Module
class ProvidesDataModule {
    @[ApplicationScope Provides]
    fun provideApiService(): ApiService = ApiFactory.apiService

    @[ApplicationScope Provides]
    fun provideDatabase(context: Context): CityDatabase = CityDatabase.getInstance(context)

    @[ApplicationScope Provides]
    fun provideCityDao(database: CityDatabase): CityDao = database.cityDao()
}