package com.example.planetapp.models

import com.example.planetapp.R

data class Planet(
    val id: Int,
    val name: String,
    val type: String,
    val galaxy: String,
    val distanceFromSun: String,
    val diameter: String,
    val characteristics: String,
    val imageRes: Int,
    var isFavorite:Boolean = false
)

val planetList = listOf(
    Planet(
        id = 1,
        name = "Mercury",
        type = "Terrestrial",
        galaxy = "Milky Way",
        distanceFromSun = "57.91 million km",
        diameter = "4880 km",
        characteristics ="Its surface is littered with craters, while its core is rich in iron.",
        imageRes = R.drawable.mercurio
    ),

    Planet(
        id = 2,
        name = "Venus",
        type = "Terrestrial",
        galaxy = "Milky Way",
        distanceFromSun = "108.2 million km",
        diameter = "12,104 km",
        characteristics ="It has an atmosphere 92 times denser than Earth's atmosphere, and the planet is almost always surrounded by clouds.",
        imageRes = R.drawable.venus
    ),

    Planet(
        id = 3,
        name = "Earth",
        type = "Terrestrial",
        galaxy = "Milky Way",
        distanceFromSun = "149.6 million km",
        diameter = "12,742 km",
        characteristics = "Supports life, has water and atmosphere.",
        imageRes = R.drawable.terra
    ),

    Planet(
        id = 4,
        name = "Mars",
        type = "Terrestrial",
        galaxy = "Milky Way",
        distanceFromSun = "227.94 million km",
        diameter = "6794 km",
        characteristics = "This planet has a climate most similar to Earth's, as well as its rotation movement.",
        imageRes = R.drawable.marte
    ),

    Planet(
        id = 5,
        name = "Jupiter",
        type = "Gaseous",
        galaxy = "Milky Way",
        distanceFromSun = "778.33 million km",
        diameter = "142,536 km",
        characteristics = "Despite being the planet with the greatest mass, it is not the densest, as it is composed of gases, especially helium and hydrogen.",
        imageRes = R.drawable.jupiter
    ),

    Planet(
        id = 6,
        name = "Saturn",
        type = "Gaseous",
        galaxy = "Milky Way",
        distanceFromSun = "1.42 billion km",
        diameter = "120,536 km",
        characteristics = "The planet's atmosphere is made up mainly of hydrogen and helium.",
        imageRes = R.drawable.saturno
    ),

    Planet(
        id = 7,
        name = "Uranus",
        type = "Gaseous",
        galaxy = "Milky Way",
        distanceFromSun = "2.87 billion km",
        diameter = "51,118 km",
        characteristics = "The planet has rings, about 27 natural satellites and about 27 moons. Its atmosphere is composed of hydrogen, helium and methane.",
        imageRes = R.drawable.urano
    ),

    Planet(
        id = 8,
        name = "Neptune",
        type = "Gaseous",
        galaxy = "Milky Way",
        distanceFromSun = "4.5 billion km",
        diameter = "49,532 km",
        characteristics = "The planet has characteristics similar to those of Uranus in terms of mass and atmospheric composition.",
        imageRes = R.drawable.netuno
    ),
    Planet(
        id = 9,
        name = "Pluto",
        type = "Gaseous",
        galaxy = "Milky Way",
        distanceFromSun = "5.9 billion km",
        diameter = "2,374 km",
        characteristics = "Pluto is a dwarf planet that orbits our solar system. It is located in a region of this system called the Kuiper Belt, in an area very far from the sun and which, therefore, has a very low influence from this star.",
        imageRes = R.drawable.plutao
    )

)