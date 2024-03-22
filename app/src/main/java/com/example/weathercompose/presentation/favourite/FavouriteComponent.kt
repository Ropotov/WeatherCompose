package com.example.weathercompose.presentation.favourite

import com.example.weathercompose.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface FavouriteComponent {
    val model: StateFlow<FavouriteStore.State>

    fun onClickSearch()

    fun onClickFavourite()

    fun onClickCity(city: City)
}