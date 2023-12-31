package com.example.filmopedia.data



data class MoviesData(
    val id: String,
    val primaryImage: Image,
    val titleText: Title,
    val releaseYear: Year
)

data class Title(
    val text: String
)

data class Image(
    val url: String
)

data class Year(
    val year: Int
)
