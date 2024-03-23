package com.example.weathercompose.di

import com.example.weathercompose.data.repository.FavouriteRepositoryImpl
import com.example.weathercompose.data.repository.SearchRepositoryImpl
import com.example.weathercompose.data.repository.WeatherRepositoryImpl
import com.example.weathercompose.domain.repository.FavouriteRepository
import com.example.weathercompose.domain.repository.SearchRepository
import com.example.weathercompose.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module

@Module
interface BindsDataModule {

    @[ApplicationScope Binds]
    fun bindsFavouriteRepository(impl: FavouriteRepositoryImpl): FavouriteRepository

    @[ApplicationScope Binds]
    fun bindsSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    @[ApplicationScope Binds]
    fun bindsWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

}