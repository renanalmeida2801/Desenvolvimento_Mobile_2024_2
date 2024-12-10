package com.example.nighteventsapp.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.nighteventsapp.R

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val isFavorite: MutableState<Boolean> = mutableStateOf(false),
    val isSubscribed: MutableState<Boolean> = mutableStateOf(false),
    val imageRes: Int
)
val eventList = listOf(
    Event(
        id = 1,
        title = "Conferência de tecnologia 2024",
        description = "Tendências em tecnologias",
        date = "2024-12-15",
        location = "Parque tecnológico",
        isFavorite = mutableStateOf(false),
        isSubscribed = mutableStateOf(false),
        imageRes = R.drawable.img1
    ),
    Event(
        id = 2,
        title = "Palestra sobre arquitetura e urbanismo moderno",
        description = "Novas formas de construir prédios altos e tecnológicos",
        date = "2024-12-21",
        location = "Universidade federal",
        isFavorite = mutableStateOf(false),
        isSubscribed = mutableStateOf(false),
        imageRes = R.drawable.img2
    ),
    Event(
        id = 3,
        title = "XV Feira de culinária",
        description = "Comidas tipicas e saborosas",
        date = "2024-12-10",
        location = "Praça Imperador Guimaraes",
        isFavorite = mutableStateOf(false),
        isSubscribed = mutableStateOf(false),
        imageRes = R.drawable.img4
    ),
    Event(
        id = 4,
        title = "Show do Linkin Park",
        description = "Novo Album da Banda Linkin Park",
        date = "2024-12-29",
        location = "Allianz Park",
        isFavorite = mutableStateOf(false),
        isSubscribed = mutableStateOf(false),
        imageRes = R.drawable.img8
    ),
    Event(
        id = 5,
        title = "3° Congresso de robótica e tecnologia",
        description = "Tema deste ano: Como a robótica podem ajudar nas tarefas domésticas",
        date = "2024-12-28",
        location = "Shopping Viena",
        isFavorite = mutableStateOf(false),
        isSubscribed = mutableStateOf(false),
        imageRes = R.drawable.new_imagem
    ),
)
