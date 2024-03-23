package com.example.weathercompose.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.example.weathercompose.presentation.details.DetailsContent
import com.example.weathercompose.presentation.favourite.FavouriteContent
import com.example.weathercompose.presentation.search.SearchContent

@Composable
fun RootContent(component: RootComponent) {

    Children(stack = component.stack) {
        when (val instance = it.instance) {
            is RootComponent.Child.Details -> DetailsContent(component = instance.component)
            is RootComponent.Child.Favourite -> FavouriteContent(component = instance.component)
            is RootComponent.Child.Search -> SearchContent(componentImpl = instance.component)

        }
    }
}