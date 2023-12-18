package com.example.filmopedia.data

data class MovieResponse(
    val id: String,
    val next: String,
    val entries: Int,
    val results: List<MoviesData>
)
