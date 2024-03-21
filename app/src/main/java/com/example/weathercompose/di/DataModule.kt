package com.example.weathercompose.di

import android.content.Context
import com.example.weathercompose.data.database.CityDao
import com.example.weathercompose.data.database.CityDatabase
import com.example.weathercompose.data.network.api.ApiFactory
import com.example.weathercompose.data.network.api.ApiService
import com.example.weathercompose.data.repository.FavouriteRepositoryImpl
import com.example.weathercompose.data.repository.SearchRepositoryImpl
import com.example.weathercompose.data.repository.WeatherRepositoryImpl
import com.example.weathercompose.domain.repository.FavouriteRepository
import com.example.weathercompose.domain.repository.SearchRepository
import com.example.weathercompose.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindsFavouriteRepository(impl: FavouriteRepositoryImpl): FavouriteRepository

    @[ApplicationScope Binds]
    fun bindsSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @[ApplicationScope Binds]
    fun bindsWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    companion object {

        @[ApplicationScope Provides]
        fun provideApiService(): ApiService = ApiFactory.apiService

        @[ApplicationScope Provides]
        fun provideDatabase(context: Context): CityDatabase = CityDatabase.getInstance(context)

        @[ApplicationScope Provides]
        fun provideCityDao(database: CityDatabase): CityDao = database.cityDao()
    }
}