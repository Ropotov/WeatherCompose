package com.example.weathercompose.presentation.favourite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weathercompose.domain.entity.City
import com.example.weathercompose.domain.useCase.GetCurrentWeatherUseCase
import com.example.weathercompose.domain.useCase.GetFavouriteCitiesUseCase
import com.example.weathercompose.presentation.favourite.FavouriteStore.Intent
import com.example.weathercompose.presentation.favourite.FavouriteStore.Label
import com.example.weathercompose.presentation.favourite.FavouriteStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavouriteStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickSearch : Intent
        data class ClickCity(val city: City) : Intent
        data object ClickAddCity : Intent
    }

    data class State(
        val cities: List<CityState>
    ) {

        data class CityState(
            val city: City,
            val weatherState: WeatherState
        )

        sealed interface WeatherState {
            data object Initial : WeatherState
            data object Loading : WeatherState
            data object Error : WeatherState
            data class Loaded(
                val temp: Float,
                val iconUrl: String
            ) : WeatherState
        }
    }

    sealed interface Label {
        data object ClickSearch : Label
        data class ClickCity(val city: City) : Label
        data object ClickAddCity : Label
    }
}

class FavouriteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
) {

    fun create(): FavouriteStore =
        object : FavouriteStore, Store<Intent, State, Label> by storeFactory.create(
            name = "FavouriteStore",
            initialState = State(listOf()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class FavouriteCityLoaded(val cities: List<City>) : Action
    }

    private sealed interface Msg {
        data class FavouriteCityLoaded(val cities: List<City>) : Msg
        data class WeatherLoaded(val cityId: Int, val temp: Float, val iconUrl: String) : Msg
        data class WeatherLoadingError(val cityId: Int) : Msg
        data class WeatherIsLoading(val cityId: Int) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavouriteCitiesUseCase.invoke().collect {
                    dispatch(Action.FavouriteCityLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ClickAddCity -> publish(Label.ClickAddCity)
                is Intent.ClickCity -> publish(Label.ClickCity(intent.city))
                Intent.ClickSearch -> publish(Label.ClickSearch)
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavouriteCityLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavouriteCityLoaded(cities))
                    cities.forEach {
                        scope.launch {
                            loadWeather(it)
                        }
                    }
                }
            }
        }

        suspend fun loadWeather(city: City) {
            dispatch(Msg.WeatherIsLoading(city.id))
            try {
                val weather = getCurrentWeatherUseCase.invoke(city.id)
                dispatch(Msg.WeatherLoaded(city.id, weather.temp, weather.urlImage))
            } catch (e: Exception) {
                dispatch(Msg.WeatherLoadingError(city.id))
            }

        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavouriteCityLoaded -> {
                copy(cities = msg.cities.map {
                    State.CityState(
                        city = it,
                        weatherState = State.WeatherState.Initial
                    )
                })
            }

            is Msg.WeatherIsLoading -> {
                copy(cities = cities.map {
                    if (it.city.id == msg.cityId) {
                        it.copy(weatherState = State.WeatherState.Loading)
                    } else {
                        it
                    }
                })
            }

            is Msg.WeatherLoaded -> {
                copy(cities = cities.map {
                    if (it.city.id == msg.cityId) {
                        it.copy(weatherState = State.WeatherState.Loaded(msg.temp, msg.iconUrl))
                    } else {
                        it
                    }
                })
            }

            is Msg.WeatherLoadingError -> {
                copy(cities = cities.map {
                    if (it.city.id == msg.cityId) {
                        it.copy(weatherState = State.WeatherState.Error)
                    } else {
                        it
                    }
                })
            }
        }
    }
}
