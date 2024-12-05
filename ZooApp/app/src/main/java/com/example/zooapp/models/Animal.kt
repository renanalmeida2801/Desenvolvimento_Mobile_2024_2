package com.example.zooapp.models

import com.example.zooapp.R

data class Animal (
    val id: Int,
    val name: String,
    val genero: String,
    val imageRes: Int,
    val description: String,
    val ondeAssistir: String,
    var isFavorite: Boolean = false
)

val animalList = listOf(
    Animal(
        id = 1,
        name = "Breaking Bad",
        genero = "Drama, Ação, Suspense, Crime",
        imageRes = R.drawable.breaking_bad,
        description = "O professor de química Walter White não acredita que sua vida possa piorar ainda mais. Quando descobre que tem câncer terminal, Walter decide arriscar tudo para ganhar dinheiro enquanto pode, transformando sua van em um laboratório de metanfetamina.",
        ondeAssistir = "Netflix."
    ),
    Animal(
        id = 2,
        name = "Game of Thrones",
        genero = "Fantasia, Drama, Ação",
        imageRes = R.drawable.game_of_thrones,
        description = "Quem se senta no trono de ferro controla os sete reinos. Game of Thrones segue a luta das famílias nobres que cobiçam esse poder.",
        ondeAssistir = "Max"
    ),
    Animal(
        id = 3,
        name = "The Boys",
        genero = "Super-Herói, Suspense, Humor ácido, Drama, Ação",
        imageRes = R.drawable.the_boys,
        description = "A Terra é habitada por super-heróis que são um inspiração para a humanidade. Porém, esses protetores têm um lado sinistro que a maioria das pessoas desconhece. Se eles usam seus poderes para o mal, Hughie, Billy e o resto do time devem detê-los.",
        ondeAssistir = "Amazon Prime Video"
    ),
)
