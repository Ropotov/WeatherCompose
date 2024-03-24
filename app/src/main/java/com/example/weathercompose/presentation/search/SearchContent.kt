package com.example.weathercompose.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weathercompose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(componentImpl: SearchComponent) {
    val state by componentImpl.model.collectAsState()

    val focusRequest = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) { focusRequest.requestFocus() }

    SearchBar(
        modifier = Modifier.focusRequester(focusRequest),
        query = state.searchQuery,
        placeholder = { Text(text = stringResource(id = R.string.search)) },
        onQueryChange = { componentImpl.changeSearchQuery(it) },
        onSearch = { componentImpl.onSearchClick() },
        active = true,
        onActiveChange = {},
        leadingIcon = {
            IconButton(onClick = { componentImpl.onClickBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.button_back_content_description)
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = { componentImpl.onSearchClick() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
        }
    ) {
        when (val state = state.searchState) {
            SearchStore.State.SearchState.EmptyResult -> {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.error_result)
                )
            }

            SearchStore.State.SearchState.Error -> {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.empty_result)
                )
            }

            SearchStore.State.SearchState.Initial -> {}
            is SearchStore.State.SearchState.Loaded -> {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.cities,
                        key = { it.id }
                    ) {
                        Card {
                            Column(
                                modifier = Modifier
                                    .clickable { componentImpl.onCityClick(it) }
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = it.name)
                                Text(text = it.country)
                            }
                        }
                    }
                }
            }

            SearchStore.State.SearchState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}