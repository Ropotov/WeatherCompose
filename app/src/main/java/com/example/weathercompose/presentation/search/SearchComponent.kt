package com.example.weathercompose.presentation.search

import com.example.weathercompose.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    val model: StateFlow<SearchStore.State>

    fun onClickBack()

    fun onSearchClick()

    fun onCityClick(city: City)

    fun changeSearchQuery(query: String)

}