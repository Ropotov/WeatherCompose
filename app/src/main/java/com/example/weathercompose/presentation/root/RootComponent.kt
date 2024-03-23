package com.example.weathercompose.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.weathercompose.presentation.details.DetailsComponent
import com.example.weathercompose.presentation.favourite.FavouriteComponent
import com.example.weathercompose.presentation.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Favourite(val component: FavouriteComponent) : Child
        data class Search(val component: SearchComponent) : Child
        data class Details(val component: DetailsComponent) : Child
    }
}