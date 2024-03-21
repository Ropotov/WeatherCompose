package com.example.weathercompose.domain.useCase

import com.example.weathercompose.domain.repository.FavouriteRepository
import javax.inject.Inject

class ObserveFavouriteStateUseCase @Inject constructor(
    private val favouriteRepository: FavouriteRepository
) {
    operator fun invoke(citiId: Int) = favouriteRepository.observeIsFavorite(citiId)
}