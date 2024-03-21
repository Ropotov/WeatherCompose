package com.example.weathercompose.domain.useCase

import com.example.weathercompose.domain.repository.FavouriteRepository
import javax.inject.Inject

class GetFavouriteCitiesUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) {
    operator fun invoke() = favouriteRepository.favoriteCities
}