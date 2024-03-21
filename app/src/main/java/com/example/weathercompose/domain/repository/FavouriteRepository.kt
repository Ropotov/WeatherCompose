package com.example.weathercompose.domain.repository

import com.example.weathercompose.domain.entity.City
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {

    val favoriteCities: Flow<List<City>>
    fun observeIsFavorite(cityId: Int): Flow<Boolean>
    suspend fun addToFavorite(city: City)
    suspend fun removeToFavorite(cityId: Int)
}