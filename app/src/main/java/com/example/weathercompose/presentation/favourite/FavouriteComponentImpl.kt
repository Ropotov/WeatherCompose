package com.example.weathercompose.presentation.favourite

import com.arkivanov.decompose.ComponentContext

class FavouriteComponentImpl(
    private val componentContext: ComponentContext
) : FavouriteComponent, ComponentContext by componentContext