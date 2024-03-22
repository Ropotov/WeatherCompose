package com.example.weathercompose.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weathercompose.domain.entity.City
import com.example.weathercompose.domain.useCase.ChangeFavouriteStateUseCase
import com.example.weathercompose.domain.useCase.SearchCityUseCase
import com.example.weathercompose.presentation.search.SearchStore.Intent
import com.example.weathercompose.presentation.search.SearchStore.Label
import com.example.weathercompose.presentation.search.SearchStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

internal interface SearchStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeSearchQuery(val query: String) : Intent
        data object ClickBack : Intent
        data class ClickCity(val city: City) : Intent
        data object ClickSearch : Intent
    }

    data class State(
        val searchQuery: String,
        val searchState: SearchState
    ) {

        sealed interface SearchState {
            data object Initial : SearchState
            data object Loading : SearchState
            data object Error : SearchState

            data object EmptyResult : SearchState
            data class Loaded(
                val cities: List<City>
            ) : SearchState
        }
    }

    sealed interface Label {
        data object ClickBack : Label
        data object SaveToFavourite : Label
        data class ClickCity(val city: City) : Label
    }
}

internal class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val searchCityUseCase: SearchCityUseCase,
    private val changeFavouriteStateUseCase: ChangeFavouriteStateUseCase,
) {

    fun create(openReason: OpenReason): SearchStore =
        object : SearchStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SearchStore",
            initialState = State(
                searchQuery = "",
                searchState = State.SearchState.Initial
            ),
            executorFactory = { ExecutorImpl(openReason) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Msg {
        data class ChangeSearchQuery(val query: String) : Msg
        data class SearchResultLoaded(val cities: List<City>) : Msg
        data object ErrorSearch : Msg
        data object LoadingSearchResult : Msg
    }


    private inner class ExecutorImpl(private val openReason: OpenReason) :
        CoroutineExecutor<Intent, Nothing, State, Msg, Label>() {

        var searchJob: Job? = null

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeSearchQuery -> dispatch(Msg.ChangeSearchQuery(intent.query))
                Intent.ClickBack -> publish(Label.ClickBack)
                is Intent.ClickCity -> {
                    if (openReason == OpenReason.AddToFavourite) {
                        scope.launch {
                            changeFavouriteStateUseCase.addFavouriteCity(intent.city)
                            publish(Label.SaveToFavourite)
                        }
                    } else {
                        publish(Label.ClickCity(city = intent.city))
                    }
                }

                Intent.ClickSearch -> {
                    searchJob?.cancel()
                    searchJob = scope.launch {
                        dispatch(Msg.LoadingSearchResult)
                        try {
                            val cities = searchCityUseCase.invoke(getState().searchQuery)
                            dispatch(Msg.SearchResultLoaded(cities))
                        } catch (e: Exception) {
                            dispatch(Msg.ErrorSearch)
                        }
                    }
                }
            }
        }

    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeSearchQuery -> copy(searchQuery = msg.query)
            Msg.ErrorSearch -> copy(searchState = State.SearchState.Error)
            Msg.LoadingSearchResult -> copy(searchState = State.SearchState.Loading)
            is Msg.SearchResultLoaded -> {
                if (msg.cities.isNotEmpty()) copy(searchState = State.SearchState.Loaded(msg.cities))
                else copy(searchState = State.SearchState.EmptyResult)
            }
        }
    }
}
