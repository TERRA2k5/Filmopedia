package com.example.filmopedia.data



data class MoviesData(
    val primaryImage: Image,
    val titleText: Title,
    val releaseYear: Year
)

data class Title(
    val text: Any
)

data class Image(
    val url: Any
)

data class Year(
    val year: Any
)
