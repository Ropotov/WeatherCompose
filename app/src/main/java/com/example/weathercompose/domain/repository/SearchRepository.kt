package com.example.weathercompose.domain.repository

import com.example.weathercompose.domain.entity.City

interface SearchRepository {
    suspend fun search(query: String): List<City>
}