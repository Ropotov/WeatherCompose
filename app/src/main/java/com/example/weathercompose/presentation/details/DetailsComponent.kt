package com.example.weathercompose.presentation.details

import com.example.weathercompose.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {
    val model: StateFlow<DetailsStore.State>

    fun onClickBack()

    fun onClickChangeFavourite()

}