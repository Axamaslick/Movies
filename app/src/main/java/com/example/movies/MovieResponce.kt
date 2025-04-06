package com.example.movies

data class MovieResponse(
    val Search: List<MovieItem>,
    val totalResults: String
)