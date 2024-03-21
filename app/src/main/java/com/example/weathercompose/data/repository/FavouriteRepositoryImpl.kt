package com.example.weathercompose.data.repository

import com.example.weathercompose.data.database.CityDao
import com.example.weathercompose.data.mappers.toCity
import com.example.weathercompose.data.mappers.toDbEntity
import com.example.weathercompose.data.mappers.toListCity
import com.example.weathercompose.domain.entity.City
import com.example.weathercompose.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val dao: CityDao
) : FavouriteRepository {
    override val favoriteCities: Flow<List<City>>
        get() = dao.getFavouriteCities().map { it.toListCity() }

    override fun observeIsFavorite(cityId: Int): Flow<Boolean> {
        return dao.observeIsFavourite(cityId)
    }

    override suspend fun addToFavorite(city: City) {
        dao.addCity(city.toDbEntity())
    }

    override suspend fun removeToFavorite(cityId: Int) {
        dao.removeCity(cityId)
    }
}