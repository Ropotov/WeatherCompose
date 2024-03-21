package com.example.weathercompose.presentation.search

import com.arkivanov.decompose.ComponentContext

class SearchComponentImpl(
    private val componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext