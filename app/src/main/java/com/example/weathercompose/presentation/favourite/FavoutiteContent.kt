package com.example.weathercompose.presentation.favourite

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weathercompose.R
import com.example.weathercompose.presentation.ui.theme.tempToFormattedString

@Composable
fun FavouriteContent(component: FavouriteComponent) {

    val state by component.model.collectAsState()

    LazyVerticalGrid(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            SearchCard {
                component.onClickSearch()
            }
        }
        items(
            items = state.cities,
            key = { it.city.id }
        ) { item ->
            CityCard(cityState = item) {
                component.onClickCity(item.city)
            }
        }
        item {
            AddToFavouriteCities {
                component.onClickFavourite()
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun CityCard(cityState: FavouriteStore.State.CityState, onClick: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Column(
            modifier = Modifier
                .sizeIn(minHeight = 196.dp)
                .fillMaxSize()
                .clickable { onClick() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (val state = cityState.weatherState) {
                FavouriteStore.State.WeatherState.Error -> {}
                FavouriteStore.State.WeatherState.Initial -> {}
                is FavouriteStore.State.WeatherState.Loaded -> {
                    Spacer(modifier = Modifier.weight(1f))
                    GlideImage(
                        model = state.iconUrl,
                        contentDescription = stringResource(R.string.icon_weather_content_description),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = state.temp.tempToFormattedString(),
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 36.sp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = cityState.city.name)
                    Spacer(modifier = Modifier.weight(1f))
                }

                FavouriteStore.State.WeatherState.Loading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchCard(onClick: () -> Unit) {
    Card(
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(R.string.search),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}

@Composable
private fun AddToFavouriteCities(
    onClick: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Column(
            modifier = Modifier
                .sizeIn(minHeight = 196.dp)
                .fillMaxSize()
                .clickable { onClick() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.add_new_favourite_city_content_description),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.add_city),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}