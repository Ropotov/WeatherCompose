package com.example.weathercompose.domain.useCase

import com.example.weathercompose.domain.entity.City
import com.example.weathercompose.domain.repository.FavouriteRepository
import javax.inject.Inject

class ChangeFavouriteStateUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) {
    suspend fun addFavouriteCity(city: City) = favouriteRepository.addToFavorite(city)
    suspend fun removeFavouriteCity(cityId: Int) = favouriteRepository.removeToFavorite(cityId)
}