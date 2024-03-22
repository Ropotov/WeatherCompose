package com.example.weathercompose.data.repository

import com.example.weathercompose.data.mappers.toCity
import com.example.weathercompose.data.mappers.toListCity
import com.example.weathercompose.data.network.api.ApiService
import com.example.weathercompose.domain.entity.City
import com.example.weathercompose.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchRepository {
    override suspend fun search(query: String): List<City> {
        return apiService.searchCity(query).toListCity()
    }
}