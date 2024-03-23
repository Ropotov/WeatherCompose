package com.example.weathercompose.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.weathercompose.domain.entity.City
import com.example.weathercompose.presentation.details.DetailsComponentImpl
import com.example.weathercompose.presentation.favourite.FavouriteComponentImpl
import com.example.weathercompose.presentation.search.OpenReason
import com.example.weathercompose.presentation.search.SearchComponentImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class RootComponentImpl @AssistedInject constructor(
    private val detailsComponentFactory: DetailsComponentImpl.Factory,
    private val searchComponentFactory: SearchComponentImpl.Factory,
    private val favouriteComponentFactory: FavouriteComponentImpl.Factory,
    @Assisted componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Favourite,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            is Config.Details -> {
                val component = detailsComponentFactory.create(
                    city = config.city,
                    onBackClick = {
                        navigation.pop()
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Details(component)
            }

            Config.Favourite -> {
                val component = favouriteComponentFactory.create(
                    onCityClick = {
                        navigation.push(Config.Details(it))
                    },
                    onSearchClick = {
                        navigation.push(Config.Search(OpenReason.Search))
                    },
                    onAddToFavouriteClick = {
                        navigation.push(Config.Search(OpenReason.AddToFavourite))
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Favourite(component)
            }

            is Config.Search -> {
                val component = searchComponentFactory.create(
                    openReason = config.openReason,
                    onBackClick = {
                        navigation.pop()
                    },
                    onClickCity = {
                        navigation.push(Config.Details(it))
                    },
                    onSaveToFavouriteClick = {
                        navigation.pop()
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Search(component)

            }
        }
    }

    sealed interface Config : Parcelable {
        @Parcelize
        data object Favourite : Config

        @Parcelize
        data class Search(val openReason: OpenReason) : Config

        @Parcelize
        data class Details(val city: City) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted componentContext: ComponentContext
        ): RootComponentImpl
    }
}