package com.example.planetapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.planetapp.models.Planet
import com.example.planetapp.models.planetList
import com.example.planetapp.ui.components.PlanetListItem
import com.example.planetapp.ui.components.TopAppBarWithMenu

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    onPlanetSelected: (Planet) -> Unit,
    onSettingsClick: () -> Unit,
    onHelpClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    // Lista filtrada, reavaliada sempre que searchQuery mudar
    val filteredPlanets by remember {
        derivedStateOf {
            planetList.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }
    }

    // Lista de buscas recentes
    val recentSearches = remember { mutableStateListOf<Planet>() }

    Scaffold(
        topBar = {
            TopAppBarWithMenu(
                onSettingsClick = onSettingsClick,
                onHelpClick = onHelpClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Barra de pesquisa
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Pesquisar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            // LazyRow para exibir buscas recentes
            LazyRow(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recentSearches) { planet ->
                    Button(onClick = { onPlanetSelected(planet) }) {
                        Text(planet.name)
                    }
                }
            }

            // LazyColumn para lista de planetas filtrados
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                items(filteredPlanets) { planet ->
                    PlanetListItem(
                        planet = planet,
                        onPlanetSelected = { selectedPlanet ->
                            // Adiciona o planeta às buscas recentes, evitando duplicatas
                            if (!recentSearches.contains(selectedPlanet)) {
                                recentSearches.add(0, selectedPlanet)
                            }
                            onPlanetSelected(selectedPlanet)
                        },
                        onFavoriteToggle = { favoritePlanet ->
                            // Alterna o estado de favorito
                            favoritePlanet.isFavorite = !favoritePlanet.isFavorite
                        }
                    )
                }
            }
        }
    }
}